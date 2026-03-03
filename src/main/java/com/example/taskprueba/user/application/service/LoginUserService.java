package com.example.taskprueba.user.application.service;

import org.springframework.stereotype.Service;

import com.example.taskprueba.user.application.command.LoginUserCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.exceptions.IncorrectPassword;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.application.usecase.LoginUserUseCase;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUserService implements LoginUserUseCase {
    
    private final GetUser getUser;

    @Override
    public UserDTO execute(LoginUserCommand command) {
        User user = getUser.getUserByAccount(command.account()).orElseThrow(() -> UserNotFound.userNotFound(command.account()));

        if (!user.isMe(command.account(), command.password())) {
            throw IncorrectPassword.incorrectPassword();
        }
        
        return UserDTO.from(user);
    }

}
