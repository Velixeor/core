package com.example.core.config;


import com.example.core.condition.MessageStarter;
import com.example.core.condition.MessagerStarterImplCamunda;
import com.example.core.condition.MessagerStarterImplEvents;
import com.example.core.condition.bean.CamundaCondition;
import com.example.core.condition.bean.EventCondition;
import com.example.core.service.BankAccountService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;


@Configuration
public class MessagerConfig {

    @Bean
    @Primary
    @Conditional(CamundaCondition.class)
    public MessageStarter messagerTypeCamunda() {
        return new MessagerStarterImplCamunda();
    }

    @Bean
    @Primary
    @Conditional(EventCondition.class)
    public MessageStarter messagerTypeEvent() {
        return new MessagerStarterImplEvents();
    }
}
