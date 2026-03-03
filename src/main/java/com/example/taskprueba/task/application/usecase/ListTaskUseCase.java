package com.example.taskprueba.task.application.usecase;

import java.util.List;

import com.example.taskprueba.task.application.dto.TaskDTO;
import com.example.taskprueba.task.application.query.ListTasksQuery;

public interface ListTaskUseCase {
    
    public List<TaskDTO> execute(ListTasksQuery query);

}
