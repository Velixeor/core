package com.example.core.events.handlers;


import com.example.core.entity.Message;
import com.example.core.events.DocumentUnmarshalledEvent;
import com.example.core.events.bankAccount.BankAccountVerifiedEvent;
import com.example.core.events.message.MessageEvent;
import com.example.core.events.message.MessageRejectedEvent;
import com.example.core.events.message.MessageSavedEvent;
import com.example.core.events.message.MessageValidatedEvent;
import com.example.core.exception.bankAccount.BankAccountCheckException;
import com.example.core.exception.moneyTransfer.PaymentCreateException;
import com.example.core.repository.MessageRepository;
import com.example.core.service.BankAccountService;
import com.example.paymentXSD.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageEventHandler {

    private final ResourceLoader resourceLoader;
    private final MessageRepository messageRepository;
    private final ApplicationEventPublisher publisher;
    private final BankAccountService bankAccountService;

    @EventListener
    public void handleMessageEvent(MessageEvent event) {
        Message message = new Message();
        message.setBody(event.getMessage());
        messageRepository.save(message);
        log.debug("Saved message: "+message);
        publisher.publishEvent(new MessageSavedEvent(this, message));
    }

    @EventListener
    public void handleMessageSavedEvent(MessageSavedEvent event) {
        Message message = event.getMessage();
        try {
            validateMessageWithJAXB(message.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            publisher.publishEvent(new MessageRejectedEvent(this, message, "Validation failed"));
        }
        log.debug("Successful validating message");
        publisher.publishEvent(new MessageValidatedEvent(this, message));
    }

    @EventListener
    public void handleMessageValidatedEvent(MessageValidatedEvent event) {
        Message message = event.getMessage();
        try {
            checkBankAccountWithXPath(message);
        } catch (XPathExpressionException e) {
            publisher.publishEvent(new MessageRejectedEvent(this, message, "Bank account not found"));
        }
        log.debug("Successful validating event");
        publisher.publishEvent(new BankAccountVerifiedEvent(this, message));
    }

    private void validateMessageWithJAXB(String xml) throws IOException, SAXException {
        Resource resource = resourceLoader.getResource("classpath:xsd/pacs.008.001.12.xsd");
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(resource.getFile());
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new StringReader(xml)));
    }

    @EventListener
    public void handleBankAccountVerifiedEvent(BankAccountVerifiedEvent event) {
        Message message = event.getMessage();
        try {
            Document document = unmarshalMessageToDocument(message);
            log.debug("Unmarshaled document:"+document.toString());
            publisher.publishEvent(new DocumentUnmarshalledEvent(this, message, document));
        } catch (Exception e) {
            log.debug("Failed unmarshaled document:"+e);
            throw new PaymentCreateException(message.getBody());
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

    private void checkBankAccountWithXPath(Message message) throws XPathExpressionException {
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

        if (bankAccountService.getBankAccountById(Integer.valueOf(bankAccountFrom)) == null) {
            throw new BankAccountCheckException(Integer.valueOf(bankAccountFrom));
        }
    }
}
