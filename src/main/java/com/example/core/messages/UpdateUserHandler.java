package com.example.core.messages;


import com.example.core.dto.UserDTO;
import com.example.core.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;


@Component
public class UpdateUserHandler implements MessageHandler {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UpdateUserHandler(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleMessage(Message message) throws Exception {
        UserDTO userDTO = objectMapper.readValue(message.getBody(), UserDTO.class);
        userService.updateUserById(userDTO);
    }
}
