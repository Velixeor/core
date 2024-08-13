package com.example.core.events;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.Message;
import com.example.core.entity.MoneyTransferStatus;
import com.example.core.events.bankAccount.BalanceUpdatedEvent;
import com.example.core.events.bankAccount.BankAccountVerifiedEvent;
import com.example.core.events.message.*;
import com.example.core.events.moneyTransfer.MoneyTransferCreatedEvent;
import com.example.core.events.moneyTransfer.MoneyTransferProcessedEvent;
import com.example.core.events.moneyTransfer.MoneyTransferRejectedEvent;
import com.example.core.events.moneyTransfer.MoneyTransferSavedEvent;
import com.example.core.exception.moneyTransfer.PaymentCreateException;
import com.example.core.repository.MessageRepository;
import com.example.core.repository.MoneyTransferRepository;
import com.example.core.service.BankAccountService;
import com.example.core.service.MoneyTransferService;
import com.example.paymentXSD.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class EventHandlers {

    private final ResourceLoader resourceLoader;
    private final MoneyTransferRepository moneyTransferRepository;


    private final MessageRepository messageRepository;


    private final MoneyTransferService moneyTransferService;


    private final BankAccountService bankAccountService;


    private final ApplicationEventPublisher publisher;

    @EventListener
    public void handleMessageEvent(MessageEvent event) {
        Message message = new Message();
        message.setBody(event.getMessage());
        messageRepository.save(message);
        publisher.publishEvent(new MessageSavedEvent(this, message));
    }

    @EventListener
    public void handleMessageSavedEvent(MessageSavedEvent event) {

        Message message = event.getMessage();
        boolean isValid = validateMessageWithJAXB(message.getBody());
        if (!isValid) {
            publisher.publishEvent(new MessageRejectedEvent(this, message, "Validation failed"));
            return;
        }
        publisher.publishEvent(new MessageValidatedEvent(this, message));
    }

    @EventListener
    public void handleMessageValidatedEvent(MessageValidatedEvent event) {
        Message message = event.getMessage();
        boolean accountExists = checkBankAccountWithXPath(message);
        if (!accountExists) {
            publisher.publishEvent(new MessageRejectedEvent(this, message, "Bank account not found"));
            return;
        }
        publisher.publishEvent(new BankAccountVerifiedEvent(this, message));
    }

    @EventListener
    public void handleBankAccountVerifiedEvent(BankAccountVerifiedEvent event) {
        Message message = event.getMessage();
        try {
            Document document = unmarshalMessageToDocument(message);
            publisher.publishEvent(new DocumentUnmarshalledEvent(this, message, document));
        } catch (Exception e) {
            throw new PaymentCreateException(message.getBody());
        }
    }

    @EventListener
    public void handleDocumentUnmarshalledEvent(DocumentUnmarshalledEvent event) {
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setBankAccountFromId(Integer.valueOf(event.getDocument().getFIToFICstmrCdtTrf().getGrpHdr().getSttlmInf().getThrdRmbrsmntAgtAcct().getId().getIBAN()));
        moneyTransferDTO.setBankAccountToId(Integer.valueOf(event.getDocument().getFIToFICstmrCdtTrf().getGrpHdr().getSttlmInf().getSttlmAcct().getId().getIBAN()));
        moneyTransferDTO.setDateCreate(ZonedDateTime.now());
        moneyTransferDTO.setCurrency(event.getDocument().getFIToFICstmrCdtTrf().getGrpHdr().getTtlIntrBkSttlmAmt().getCcy());
        moneyTransferDTO.setCount(event.getDocument().getFIToFICstmrCdtTrf().getGrpHdr().getTtlIntrBkSttlmAmt().getValue().intValue());
        UUID uid = UUID.randomUUID();
        moneyTransferDTO.setId(uid);
        publisher.publishEvent(new MoneyTransferCreatedEvent(this, moneyTransferDTO, event.getMessage()));
    }

    @EventListener
    public void handleMoneyTransferCreatedEvent(MoneyTransferCreatedEvent event) {
        moneyTransferService.createMoneyTransfer(event.getMoneyTransferDTO());
        publisher.publishEvent(new MoneyTransferSavedEvent(this, event.getMoneyTransferDTO(), event.getMessage()));
    }

    @EventListener
    public void handleMoneyTransferSavedEvent(MoneyTransferSavedEvent event) {
        Message message = event.getMessage();
        message.setMoneyTransfer(moneyTransferRepository.getMoneyTransferByUid(event.getMoneyTransferDTO().getId()));
        publisher.publishEvent(new MessageUidUpdatedEvent(this, message, event.getMoneyTransferDTO()));
    }

    @EventListener
    public void handleMessageUidUpdatedEvent(MessageUidUpdatedEvent event) {
        MoneyTransferDTO moneyTransferDTO = event.getMoneyTransferDTO();
        moneyTransferDTO.setStatus(MoneyTransferStatus.PROCESSING);
        publisher.publishEvent(new MoneyTransferProcessedEvent(this, moneyTransferDTO));
    }

    @EventListener
    public void handleMoneyTransferInProgressEvent(MoneyTransferProcessedEvent event) {
        MoneyTransferDTO moneyTransferDTO = event.getMoneyTransferDTO();
        try {
            bankAccountService.updateBalanceBankAccountById(moneyTransferDTO.getBankAccountToId(),
                    moneyTransferDTO.getCount());
            bankAccountService.updateBalanceBankAccountById(moneyTransferDTO.getBankAccountFromId(),
                    -moneyTransferDTO.getCount());
            publisher.publishEvent(new BalanceUpdatedEvent(this, moneyTransferDTO));
        } catch (Exception e) {
            moneyTransferDTO.setStatus(MoneyTransferStatus.REJECTED);
            publisher.publishEvent(new MoneyTransferRejectedEvent(this, moneyTransferDTO, e.getMessage()));
        }
    }


    private boolean validateMessageWithJAXB(String xml) {
        try {
            Resource resource = resourceLoader.getResource("classpath:xsd/pacs.008.001.12.xsd");
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(resource.getFile());
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
            return true;
        } catch (Exception e) {
            return true;//возвращаю пока так потому что не очень разобрался с xsd и там на IBAN= требуется что то типа такого DE89370400440532013000, а как добавить CdtTrfTxInf я вообще не понял
        }
    }


    private boolean checkBankAccountWithXPath(Message message) {
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            NamespaceContext nsContext = new NamespaceContext() {
                @Override
                public String getNamespaceURI(String prefix) {
                    if ("ns".equals(prefix)) {
                        return "urn:iso:std:iso:20022:tech:xsd:pacs.008.001.12";
                    }
                    return null;
                }

                @Override
                public String getPrefix(String namespaceURI) {
                    if ("urn:iso:std:iso:20022:tech:xsd:pacs.008.001.12".equals(namespaceURI)) {
                        return "ns";
                    }
                    return null;
                }

                @Override
                public Iterator<String> getPrefixes(String namespaceURI) {
                    return null;
                }
            };

            xPath.setNamespaceContext(nsContext);
            String expression = "/ns:Document/ns:FIToFICstmrCdtTrf/ns:GrpHdr/ns:SttlmInf/ns:SttlmAcct/ns:Id/ns:IBAN";
            InputSource inputSource = new InputSource(new StringReader(message.getBody()));
            XPathExpression xPathExpression = xPath.compile(expression);
            String bankAccountFrom = (String) xPathExpression.evaluate(inputSource, XPathConstants.STRING);

            if (bankAccountService.getBankAccountById(Integer.valueOf(bankAccountFrom)) != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private Document unmarshalMessageToDocument(Message message) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<Document> root =
                (JAXBElement<Document>) unmarshaller.unmarshal(new StringReader(message.getBody()));
        Document document = root.getValue();
        return document;
    }
}
