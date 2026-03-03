package com.example.taskprueba.user.application.usecase;

import com.example.taskprueba.user.application.command.ChangeUserDataCommand;
import com.example.taskprueba.user.application.dto.UserDTO;

public interface ChangeUserDataUseCase {

    public UserDTO execute(ChangeUserDataCommand command);

}
