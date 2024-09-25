package com.example.delegats;


import com.example.delegats.messages.CamundaMessage;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;

@Component
@RequiredArgsConstructor
public class ValidateMessageDelegate implements JavaDelegate {
    private final ResourceLoader resourceLoader;
    @Override
    public void execute(DelegateExecution execution) {
        String camundajson = (String) execution.getVariable("savedM");
        CamundaMessage savedMessage = CamundaMessage.fromJson(camundajson);

        try {
            validateMessageWithJAXB(savedMessage.getMessage().getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {

        }
            execution.setVariable("v", savedMessage.toJson());
    }
    private void validateMessageWithJAXB (String xml) throws IOException, SAXException {
        Resource resource = resourceLoader.getResource("classpath:xsd/pacs.008.001.12.xsd");
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(resource.getFile());
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new StringReader(xml)));
    }
}
