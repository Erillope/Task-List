package com.example.taskprueba.task.domain.persistence_ports;

import java.util.List;
import java.util.Optional;

import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.values.TaskFilterQuery;

public interface GetTask {
    
    public boolean existsById(String id);

    public Optional<Task> getTaskById(String id);

    public List<Task> filterTasks(TaskFilterQuery query);

    public List<Task> getTasksByUserId(String userId);

}
