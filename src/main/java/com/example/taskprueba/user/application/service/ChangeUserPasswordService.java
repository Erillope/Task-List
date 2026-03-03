package com.example.taskprueba.user.application.service;

import org.springframework.stereotype.Service;

import com.example.taskprueba.user.application.command.ChangeUserPasswordCommand;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.application.usecase.ChangeUserPasswordUseCase;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;
import com.example.taskprueba.user.domain.persistence_ports.SaveUser;
import com.example.taskprueba.user.domain.values.Password;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChangeUserPasswordService implements ChangeUserPasswordUseCase {
    
    private final GetUser getUser;
    private final SaveUser saveUser;

    @Override
    public void execute(ChangeUserPasswordCommand command) {
        User user = getUser.getUserById(command.id()).orElseThrow(() -> UserNotFound.userNotFound(command.id()));
        
        user.changePassword(Password.of(command.newPassword()));
        
        saveUser.save(user);
    }

}
