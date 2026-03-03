package com.example.taskprueba.task.application.usecase;

import com.example.taskprueba.task.application.command.StartTaskCommand;
import com.example.taskprueba.task.application.dto.TaskDTO;

public interface StartTaskUseCase {
    
    public TaskDTO execute(StartTaskCommand command);

}
