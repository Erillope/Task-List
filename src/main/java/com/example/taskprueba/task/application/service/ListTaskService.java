package com.example.taskprueba.task.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskprueba.common.values.NonNegativeNumber;
import com.example.taskprueba.task.application.dto.TaskDTO;
import com.example.taskprueba.task.application.query.ListTasksQuery;
import com.example.taskprueba.task.application.usecase.ListTaskUseCase;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.persistence_ports.GetTask;
import com.example.taskprueba.task.domain.values.TaskFilterQuery;
import com.example.taskprueba.user.domain.values.UserID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListTaskService implements ListTaskUseCase{
    
    private final GetTask getTask;

    @Override
    public List<TaskDTO> execute(ListTasksQuery query) {
        List<Task> tasks = getTask.filterTasks(buildQuery(query));

        return tasks.stream()
                .map(task -> TaskDTO.fromTask(task, query.userId()))
                .toList();
                
    }

    private TaskFilterQuery buildQuery(ListTasksQuery query) {
        return new TaskFilterQuery(
            new UserID(query.userId()),
            query.scheduledDate(),
            new NonNegativeNumber(query.page()),
            new NonNegativeNumber(query.size())
        );
    }
}
