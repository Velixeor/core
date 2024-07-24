package com.example.core.service;


import com.example.core.dto.UserDTO;
import com.example.core.entity.User;
import com.example.core.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setLogin(userDTO.getLogin());
        user.setStatus(userDTO.getStatus());
        User userResult = userRepository.save(user);
        UserDTO userDTOResult = new UserDTO(userResult.getId(), userResult.getLogin(), userResult.getStatus());
        return userDTOResult;
    }

    public UserDTO updateUserById(UserDTO userDTO) {
        User user = userRepository.getUserById(userDTO.getId());
        user.setLogin(userDTO.getLogin());
        user.setStatus(userDTO.getStatus());

        User userResult = userRepository.save(user);

        UserDTO userDTOResult = new UserDTO(userResult.getId(), userResult.getLogin(), userResult.getStatus());
        return userDTOResult;
    }

}