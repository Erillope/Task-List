package com.example.taskprueba.task.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskprueba.task.application.dto.TaskDTO;
import com.example.taskprueba.task.application.query.ListTasksQuery;
import com.example.taskprueba.task.application.usecase.ListTaskUseCase;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.persistence_ports.GetTask;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListTaskService implements ListTaskUseCase{
    
    private final GetTask getTask;

    @Override
    public List<TaskDTO> execute(ListTasksQuery query) {
        List<Task> tasks = getTask.getTasksByUserId(query.userId());

        return tasks.stream()
                .map(task -> TaskDTO.fromTask(task, query.userId()))
                .toList();
                
    }
}
