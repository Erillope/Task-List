package com.example.taskprueba.task.application.service;

import org.springframework.stereotype.Service;

import com.example.taskprueba.task.application.command.RemoveTaskCommand;
import com.example.taskprueba.task.application.exceptions.TaskNotFound;
import com.example.taskprueba.task.application.usecase.RemoveTaskUseCase;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.persistence_ports.DeleteTask;
import com.example.taskprueba.task.domain.persistence_ports.GetTask;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;
import com.example.taskprueba.user.domain.persistence_ports.SaveUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RemoveTaskService implements RemoveTaskUseCase{
    
    private final GetUser getUser;
    private final SaveUser saveUser;
    private final GetTask getTask;
    private final DeleteTask deleteTask;

    @Override
    public void execute(RemoveTaskCommand command) {
        User user = getUser.getUserById(command.userId()).orElseThrow(() -> UserNotFound.userNotFound(command.userId()));
        Task task = getTask.getTaskById(command.taskId()).orElseThrow(() -> TaskNotFound.taskNotFound(command.taskId()));

        if (task.isPending()) {
            user.removePendingTask(task);
            saveUser.save(user);
        }

        deleteTask.delete(command.taskId());
    }

}
