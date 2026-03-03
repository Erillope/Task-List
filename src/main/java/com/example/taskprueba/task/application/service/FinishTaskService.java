package com.example.taskprueba.task.application.service;

import org.springframework.stereotype.Service;

import com.example.taskprueba.task.application.command.FinishTaskCommand;
import com.example.taskprueba.task.application.dto.TaskDTO;
import com.example.taskprueba.task.application.exceptions.TaskNotFound;
import com.example.taskprueba.task.application.usecase.FinishTaskUseCase;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.persistence_ports.GetTask;
import com.example.taskprueba.task.domain.persistence_ports.SaveTask;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;
import com.example.taskprueba.user.domain.persistence_ports.SaveUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinishTaskService implements FinishTaskUseCase{
    
    private final GetUser getUser;
    private final GetTask getTask;
    private final SaveTask saveTask;
    private final SaveUser saveUser;

    @Override
    public TaskDTO execute(FinishTaskCommand command) {
        User user = getUser.getUserById(command.userId()).orElseThrow(() -> UserNotFound.userNotFound(command.userId()));
        Task task = getTask.getTaskById(command.taskId()).orElseThrow(() -> TaskNotFound.taskNotFound(command.taskId()));
        
        task.complete();
        user.finishTask();

        saveTask.save(task);
        saveUser.save(user);

        return TaskDTO.fromTask(task, command.userId());
    }

}
