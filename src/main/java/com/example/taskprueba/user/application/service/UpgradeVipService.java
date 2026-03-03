package com.example.taskprueba.user.application.service;

import org.springframework.stereotype.Service;

import com.example.taskprueba.user.application.command.UpgradeVipCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.application.usecase.UpgradeVipUseCase;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;
import com.example.taskprueba.user.domain.persistence_ports.SaveUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpgradeVipService implements UpgradeVipUseCase {
    
    private final GetUser getUser;
    private final SaveUser saveUser;

    @Override
    public UserDTO execute(UpgradeVipCommand command) {
        User user = getUser.getUserById(command.id()).orElseThrow(() -> UserNotFound.userNotFound(command.id()));
        user.upgradeToVip();
        saveUser.save(user);

        return UserDTO.from(user);
    }
}
