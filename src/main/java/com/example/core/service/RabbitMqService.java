package com.example.core.service;


import com.example.core.exception.moneyTransfer.PaymentCreateException;
import com.example.core.messages.MessageHandler;
import com.example.core.messages.MessageHandlerFactory;
import com.example.paymentXSD.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;


@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMqService {
    private final MessageHandlerFactory messageHandlerFactory;

    @RabbitListener(queues = "QueuePayments")
    @Retryable(backoff = @Backoff(delay = 1000))
    @Transactional
    public void receiveMessage(Message message) {
        MessageProperties properties = message.getMessageProperties();
        String messageType = properties.getHeader("messageType");
        messageType = messageType + "Handler";
        try {
            MessageHandler handler = messageHandlerFactory.getHandler(messageType);
            if (handler != null) {
                handler.handleMessage(message);
            } else {
                log.warn("Unknown message type: {}", messageType);
            }
        } catch (Exception e) {
            log.error("Error processing message", e);
        }
    }

    @RabbitListener(queues = "PaymentXml")
    @Retryable(backoff = @Backoff(delay = 1000))
    @Transactional
    public void receivePaymentXMl(String paymentXML) {
        try {
            log.debug("Original XML: {}", paymentXML);


            JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            JAXBElement<Document> root =
                    (JAXBElement<Document>) unmarshaller.unmarshal(new StringReader(paymentXML));

            Document document = root.getValue();


            log.info("Document is: {}", document);
        } catch (Exception e) {
            log.error("Error processing message", e);
            throw new PaymentCreateException(paymentXML);
        }
    }

}
