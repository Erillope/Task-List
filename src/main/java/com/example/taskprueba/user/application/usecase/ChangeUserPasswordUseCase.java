package com.example.taskprueba.user.application.usecase;

import com.example.taskprueba.user.application.command.ChangeUserPasswordCommand;

public interface ChangeUserPasswordUseCase {
    
    public void execute(ChangeUserPasswordCommand command);

}
