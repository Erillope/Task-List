package com.example.taskprueba.user.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskprueba.user.application.command.ChangeUserDataCommand;
import com.example.taskprueba.user.application.command.ChangeUserPasswordCommand;
import com.example.taskprueba.user.application.command.RemoveVipCommand;
import com.example.taskprueba.user.application.command.UpgradeVipCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.usecase.ChangeUserDataUseCase;
import com.example.taskprueba.user.application.usecase.ChangeUserPasswordUseCase;
import com.example.taskprueba.user.application.usecase.RemoveVipUseCase;
import com.example.taskprueba.user.application.usecase.UpgradeVipUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final ChangeUserDataUseCase changeUserDataUseCase;
    private final ChangeUserPasswordUseCase changeUserPasswordUseCase;
    private final UpgradeVipUseCase upgradeVipUseCase;
    private final RemoveVipUseCase removeVipUseCase;

    @PutMapping
    public ResponseEntity<UserDTO> update(@Valid @RequestBody ChangeUserDataCommand command) {
        UserDTO userDTO = changeUserDataUseCase.execute(command);
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/password")
    public void changePassword(@Valid @RequestBody ChangeUserPasswordCommand command) {
        changeUserPasswordUseCase.execute(command);
    }

    @PatchMapping("/upgrade-vip")
    public ResponseEntity<UserDTO> upgradeVip(@Valid @RequestBody UpgradeVipCommand command) {
        UserDTO userDTO = upgradeVipUseCase.execute(command);
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/remove-vip")
    public ResponseEntity<UserDTO> removeVip(@Valid @RequestBody RemoveVipCommand command) {
        UserDTO userDTO = removeVipUseCase.execute(command);
        return ResponseEntity.ok(userDTO);
    }

}
