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
    public boolean sendSynchronizedBillingRequest(CommissionDTO commissionDTO) {
        ManagedChannel channel = null;
        boolean isSuccess = false;

        try {
            channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                    .usePlaintext()
                    .build();

            CommissionServiceGrpc.CommissionServiceBlockingStub commissionStub = CommissionServiceGrpc.newBlockingStub(channel);

            CommissionServiceProto.CommissionRequest request = CommissionServiceProto.CommissionRequest.newBuilder()
                    .setId(commissionDTO.getId())
                    .setFromWhom(commissionDTO.getFromWhom())
                    .setToWhom(commissionDTO.getToWhom())
                    .setAmount(commissionDTO.getAmount().doubleValue())
                    .setCurrency(commissionDTO.getCurrency())
                    .build();


            CommissionServiceProto.CommissionResponse response = commissionStub.sendCommission(request);


            if (response.getSuccess()) {
                System.out.println("Commission processed successfully.");
                isSuccess = true;
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

        return isSuccess;
    }
}
