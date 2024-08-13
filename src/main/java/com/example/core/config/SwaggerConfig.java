package com.example.core.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;


@OpenAPIDefinition(
        info = @Info(
                title = "Сore System Api",
                description = "Core System", version = "1.0.0",
                contact = @Contact(
                        name = "Tsygankov Egor",
                        email = "egor-tsygankov@mail.ru",
                        url = "https://test.ru"
                )
        )
)
public class SwaggerConfig {

}
