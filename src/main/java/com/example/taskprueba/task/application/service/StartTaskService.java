package com.example.taskprueba.task.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskprueba.task.application.command.StartTaskCommand;
import com.example.taskprueba.task.application.dto.TaskDTO;
import com.example.taskprueba.task.application.exceptions.TaskNotFound;
import com.example.taskprueba.task.application.usecase.StartTaskUseCase;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.persistence_ports.GetTask;
import com.example.taskprueba.task.domain.persistence_ports.SaveTask;
import com.example.taskprueba.task.domain.policy.StartTaskPolicy;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StartTaskService implements StartTaskUseCase{
    
    private final GetUser getUser;
    private final GetTask getTask;
    private final SaveTask saveTask;
    private final StartTaskPolicy startTaskPolicy;

    @Override
    public TaskDTO execute(StartTaskCommand command) {
        User user = getUser.getUserById(command.userId()).orElseThrow(() -> UserNotFound.userNotFound(command.userId()));
        List<Task> userTasks = getTask.getTasksByUserId(command.userId());
        Task task = findTask(command.taskId(), userTasks);
        
        task.start();
        startTaskPolicy.execute(user, userTasks);
        
        saveTask.save(task);

        return TaskDTO.fromTask(task, command.userId());
    }

    private Task findTask(String taskId, List<Task> userTasks) {
        return userTasks.stream()
                .filter(t -> t.getId().getValue().equals(taskId))
                .findFirst()
                .orElseThrow(() -> TaskNotFound.taskNotFound(taskId));
    }

}
