package com.example.delegats;


import com.example.core.entity.Message;
import com.example.core.exception.bankAccount.BankAccountCheckException;
import com.example.core.service.BankAccountService;
import com.example.delegats.messages.CamundaMessage;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.*;
import java.io.StringReader;
import java.util.Iterator;
@Component
@RequiredArgsConstructor
public class BankAccountVerifiedDelegate implements JavaDelegate {
    private final BankAccountService bankAccountService;
    @Override
    public void execute(DelegateExecution execution) {
        CamundaMessage camundaMessage = CamundaMessage.fromJson((String) execution.getVariable("v"));
        Message message = camundaMessage.getMessage();
        try {
            checkBankAccountWithXPath(message);
        } catch (XPathExpressionException e) {

        }
        execution.setVariable("b", camundaMessage.toJson());
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
