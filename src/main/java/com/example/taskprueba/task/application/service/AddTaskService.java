package com.example.taskprueba.task.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskprueba.task.application.command.AddTaskCommand;
import com.example.taskprueba.task.application.dto.TaskDTO;
import com.example.taskprueba.task.application.usecase.AddTaskUseCase;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.persistence_ports.GetTask;
import com.example.taskprueba.task.domain.persistence_ports.SaveTask;
import com.example.taskprueba.task.domain.policy.VipPriorityPolice;
import com.example.taskprueba.task.domain.values.TaskCreationData;
import com.example.taskprueba.task.domain.values.TaskName;
import com.example.taskprueba.task.domain.values.TaskPriority;
import com.example.taskprueba.task.domain.values.TaskSchedule;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;
import com.example.taskprueba.user.domain.persistence_ports.SaveUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddTaskService implements AddTaskUseCase{
    
    private final GetUser getUser;
    private final GetTask getTask;
    private final SaveTask saveTask;
    private final SaveUser saveUser;
    private final VipPriorityPolice vipPriorityPolice;

    @Override
    public TaskDTO execute(AddTaskCommand command) {
        User user = getUser.getUserById(command.userId()).orElseThrow(() -> UserNotFound.userNotFound(command.userId()));
        Task task = createTask(command);
        List<Task> userTasks = new ArrayList<>(getTask.getTasksByUserId(command.userId()));
        userTasks.add(task);

        vipPriorityPolice.execute(user, userTasks);
        user.addPendingTask(task);

        saveTask.save(task);
        saveUser.save(user);
        
        return TaskDTO.fromTask(task, command.userId());
    }

    private Task createTask(AddTaskCommand command) {
        TaskSchedule schedule = new TaskSchedule(
            command.taskScheduledDate(),
            command.taskScheduledStartTime(),
            command.taskScheduledEndTime()
        );
        TaskCreationData taskCreationData = new TaskCreationData(
            new TaskName(command.taskName()),
            TaskPriority.fromValue(command.taskPriority()),
            schedule
        );
        return Task.create(taskCreationData);
    }

}
