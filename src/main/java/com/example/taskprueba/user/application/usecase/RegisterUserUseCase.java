package com.example.taskprueba.user.application.usecase;

import com.example.taskprueba.user.application.command.RegisterUserCommand;
import com.example.taskprueba.user.application.dto.UserDTO;

public interface RegisterUserUseCase {
    
    public UserDTO execute(RegisterUserCommand command);

}
