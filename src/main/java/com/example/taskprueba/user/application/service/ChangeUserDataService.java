package com.example.taskprueba.user.application.service;

import org.springframework.stereotype.Service;

import com.example.taskprueba.user.application.command.ChangeUserDataCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.application.usecase.ChangeUserDataUseCase;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;
import com.example.taskprueba.user.domain.persistence_ports.SaveUser;
import com.example.taskprueba.user.domain.values.Account;
import com.example.taskprueba.user.domain.values.UserName;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChangeUserDataService implements ChangeUserDataUseCase{

    private final GetUser getUser;
    private final SaveUser saveUser;
    
    @Override
    public UserDTO execute(ChangeUserDataCommand command) {
        User user = getUser.getUserById(command.id()).orElseThrow(() -> UserNotFound.userNotFound(command.id()));
        
        if (command.newAccount() != null)  user.changeAccount(Account.of(command.newAccount()));
        if (command.newUserName() != null) user.changeUserName(new UserName(command.newUserName()));
        
        saveUser.save(user);

        return UserDTO.from(user);
    }
}
