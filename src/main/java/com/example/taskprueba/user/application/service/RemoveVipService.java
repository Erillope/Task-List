package com.example.taskprueba.user.application.service;

import org.springframework.stereotype.Service;

import com.example.taskprueba.user.application.command.RemoveVipCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.application.usecase.RemoveVipUseCase;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;
import com.example.taskprueba.user.domain.persistence_ports.SaveUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RemoveVipService implements RemoveVipUseCase {
    
    private final GetUser getUser;
    private final SaveUser saveUser;

    @Override
    public UserDTO execute(RemoveVipCommand command) {
        User user = getUser.getUserById(command.id()).orElseThrow(() -> UserNotFound.userNotFound(command.id()));
        user.removeVip();
        saveUser.save(user);

        return UserDTO.from(user);
    }

}
