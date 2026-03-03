package com.example.taskprueba.user.application.service;

import org.springframework.stereotype.Service;

import com.example.taskprueba.user.application.command.RegisterUserCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.exceptions.UserAlreadyRegistered;
import com.example.taskprueba.user.application.usecase.RegisterUserUseCase;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;
import com.example.taskprueba.user.domain.persistence_ports.SaveUser;
import com.example.taskprueba.user.domain.values.Account;
import com.example.taskprueba.user.domain.values.Password;
import com.example.taskprueba.user.domain.values.UserCreationData;
import com.example.taskprueba.user.domain.values.UserName;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase{
    
    private final GetUser getUser;
    private final SaveUser saveUser;

    @Override
    public UserDTO execute(RegisterUserCommand command) {
        if (getUser.existsByAccount(command.account())) throw UserAlreadyRegistered.userAlreadyRegistered(command.account());

        User user = createUser(command);
        saveUser.save(user);
        return UserDTO.from(user);
    }

    private User createUser(RegisterUserCommand command) {
        UserName userName = new UserName(command.userName());
        Account account = Account.of(command.account());
        Password password = Password.of(command.password());
        UserCreationData creationData = new UserCreationData(userName, account, password);
        return User.create(creationData);
    }

}
