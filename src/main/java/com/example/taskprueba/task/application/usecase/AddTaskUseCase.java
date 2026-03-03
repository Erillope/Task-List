package com.example.taskprueba.task.application.usecase;

import com.example.taskprueba.task.application.command.AddTaskCommand;
import com.example.taskprueba.task.application.dto.TaskDTO;

public interface AddTaskUseCase {
    
    public TaskDTO execute(AddTaskCommand command);

}
