package com.example.core.service;


import com.example.core.dto.CommissionDTO;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.grpc.service.CommissionServiceGrpc;
import my.grpc.service.CommissionServiceProto;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class GrpcService {

    public void sendSynchronizedBillingRequest(CommissionDTO commissionDTO){
        ManagedChannel channel = null;

        try {

            channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                    .usePlaintext() // Отключаем TLS для локальной разработки
                    .build();


            CommissionServiceGrpc.CommissionServiceBlockingStub commissionStub = CommissionServiceGrpc.newBlockingStub(channel);


            CommissionServiceProto.CommissionRequest request = CommissionServiceProto.CommissionRequest.newBuilder()
                    .setId(commissionDTO.getId())            // Идентификатор комиссии
                    .setFromWhom(commissionDTO.getFromWhom())         // Идентификатор отправителя
                    .setToWhom(commissionDTO.getToWhom())           // Идентификатор получателя
                    .setAmount(commissionDTO.getAmount().doubleValue())        // Сумма комиссии
                    .setCurrency(commissionDTO.getCurrency())       // Валюта комиссии
                    .build();

            // Отправляем запрос через gRPC и получаем ответ
            CommissionServiceProto.CommissionResponse response = commissionStub.sendCommission(request);

            // Обрабатываем ответ
            if (response.getSuccess()) {
                System.out.println("Commission processed successfully.");
            } else {
                System.out.println("Failed to process commission: " + response.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error while sending gRPC request: " + e.getMessage());
        } finally {

            if (channel != null) {
                channel.shutdown();
            }
        }
    }
}
