package com.example.taskprueba.task.application.usecase;

import com.example.taskprueba.task.application.command.ChangeTaskDataCommand;
import com.example.taskprueba.task.application.dto.TaskDTO;

public interface ChangeTaskDataUseCase {
    
    public TaskDTO execute(ChangeTaskDataCommand command);

}
