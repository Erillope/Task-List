package com.example.taskprueba.task.application.usecase;

import com.example.taskprueba.task.application.command.FinishTaskCommand;
import com.example.taskprueba.task.application.dto.TaskDTO;

public interface FinishTaskUseCase {
    
    public TaskDTO execute(FinishTaskCommand command);

}
