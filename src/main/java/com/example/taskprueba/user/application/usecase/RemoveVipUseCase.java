package com.example.taskprueba.user.application.usecase;

import com.example.taskprueba.user.application.command.RemoveVipCommand;
import com.example.taskprueba.user.application.dto.UserDTO;

public interface RemoveVipUseCase {

    public UserDTO execute(RemoveVipCommand command);

}
