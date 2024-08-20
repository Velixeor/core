package com.example.delegats.messages;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.Message;
import com.example.paymentXSD.Document;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class CamundaMessage {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .registerModule(new JavaTimeModule());
    private  Message message;
    private  MoneyTransferDTO moneyTransferDTO;
    private  Document document;
    public CamundaMessage(MoneyTransferDTO moneyTransferDTO, Message message,Document document) {

        this.message = message;
        this.moneyTransferDTO =  moneyTransferDTO;
        this.document=document;
    }
    public CamundaMessage() {}
    public Message getMessage() {
        return message;
    }
    public MoneyTransferDTO getMoneyTransferDTO() {
        return moneyTransferDTO;
    }
    public Document getDocument() {
        return document;
    }
    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при сериализации CamundaMessage в JSON", e);
        }
    }
    public static CamundaMessage fromJson(String json) {
        try {
            return objectMapper.readValue(json, CamundaMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при десериализации JSON в CamundaMessage", e);
        }
    }
}
