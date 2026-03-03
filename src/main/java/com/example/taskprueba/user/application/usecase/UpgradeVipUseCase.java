package com.example.taskprueba.user.application.usecase;

import com.example.taskprueba.user.application.command.UpgradeVipCommand;
import com.example.taskprueba.user.application.dto.UserDTO;

public interface UpgradeVipUseCase {
    
    public UserDTO execute(UpgradeVipCommand command);

}
