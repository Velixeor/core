package com.example.delegats;


import com.example.core.entity.Message;
import com.example.core.events.DocumentUnmarshalledEvent;
import com.example.core.exception.moneyTransfer.PaymentCreateException;
import com.example.delegats.messages.CamundaMessage;
import com.example.paymentXSD.Document;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@Component
public class DocumentUnmarshalledDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        CamundaMessage camundaMessage =  CamundaMessage.fromJson((String) execution.getVariable("b"));
        Message message = camundaMessage.getMessage();
        try {
            Document document = unmarshalMessageToDocument(message);
            CamundaMessage documentUnmarshalledMessage=new CamundaMessage(null,camundaMessage.getMessage(),document);
            execution.setVariable("d",  documentUnmarshalledMessage.toJson());
        } catch (Exception e) {
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
}
