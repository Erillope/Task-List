package com.example.taskprueba.user.application.usecase;

import com.example.taskprueba.user.application.command.LoginUserCommand;
import com.example.taskprueba.user.application.dto.UserDTO;

public interface LoginUserUseCase {
    
    public UserDTO execute(LoginUserCommand command);

}
