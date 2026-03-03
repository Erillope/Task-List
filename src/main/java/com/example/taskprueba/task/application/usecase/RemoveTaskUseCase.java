package com.example.taskprueba.task.application.usecase;

import com.example.taskprueba.task.application.command.RemoveTaskCommand;

public interface RemoveTaskUseCase {
    
    public void execute(RemoveTaskCommand command);

}
