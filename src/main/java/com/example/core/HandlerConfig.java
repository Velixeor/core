package com.example.core;


import com.example.core.messages.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class HandlerConfig {

    @Bean
    public Map<String, MessageHandler> handlers(
            SynchronizationHandler synchronizationHandler,
            CreateUserHandler createUserHandler,
            UpdateUserHandler updateUserHandler,
            CreateBankAccountHandler createBankAccountHandler) {
        Map<String, MessageHandler> handlers = new HashMap<>();
        handlers.put("synchronization", synchronizationHandler);
        handlers.put("createUser", createUserHandler);
        handlers.put("updateUser", updateUserHandler);
        handlers.put("createBankAccount", createBankAccountHandler);
        return handlers;
    }
}
