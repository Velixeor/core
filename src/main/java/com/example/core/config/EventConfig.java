package com.example.core.config;


import com.example.core.repository.MessageRepository;
import com.example.core.repository.MoneyTransferRepository;
import com.example.core.service.BankAccountService;
import com.example.core.service.MoneyTransferService;
import com.example.core.events.EventHandlers;
import com.example.core.events.EventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;


@Configuration
public class EventConfig {

    @Bean
    public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new EventPublisher(applicationEventPublisher);
    }
    @Bean
    public EventHandlers eventHandlers(ResourceLoader resourceLoader,
                                       MoneyTransferRepository moneyTransferRepository,
                                       MessageRepository messageRepository,
                                       MoneyTransferService moneyTransferService,
                                       BankAccountService bankAccountService,
                                       ApplicationEventPublisher publisher) {
        return new EventHandlers(resourceLoader, moneyTransferRepository, messageRepository,
                moneyTransferService, bankAccountService, publisher);
    }
}
