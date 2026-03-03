package com.example.taskprueba.user.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskprueba.user.application.command.LoginUserCommand;
import com.example.taskprueba.user.application.command.RegisterUserCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.usecase.LoginUserUseCase;
import com.example.taskprueba.user.application.usecase.RegisterUserUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterUserCommand command) {
        UserDTO userDTO = registerUserUseCase.execute(command);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginUserCommand command) {
        UserDTO userDTO = loginUserUseCase.execute(command);
        return ResponseEntity.ok(userDTO);
    }

}
