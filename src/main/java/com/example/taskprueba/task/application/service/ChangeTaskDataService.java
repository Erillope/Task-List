package com.example.taskprueba.task.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskprueba.task.application.command.ChangeTaskDataCommand;
import com.example.taskprueba.task.application.dto.TaskDTO;
import com.example.taskprueba.task.application.exceptions.TaskNotFound;
import com.example.taskprueba.task.application.usecase.ChangeTaskDataUseCase;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.persistence_ports.GetTask;
import com.example.taskprueba.task.domain.persistence_ports.SaveTask;
import com.example.taskprueba.task.domain.policy.VipPriorityPolice;
import com.example.taskprueba.task.domain.values.TaskName;
import com.example.taskprueba.task.domain.values.TaskPriority;
import com.example.taskprueba.task.domain.values.TaskSchedule;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChangeTaskDataService implements ChangeTaskDataUseCase{
    
    private final GetUser getUser;
    private final GetTask getTask;
    private final SaveTask saveTask;
    private final VipPriorityPolice vipPriorityPolice;

    @Override
    public TaskDTO execute(ChangeTaskDataCommand command) {
        User user = getUser.getUserById(command.userId()).orElseThrow(() -> UserNotFound.userNotFound(command.userId()));
        List<Task> userTasks = getTask.getTasksByUserId(command.userId());
        Task targetTask = findTask(command.taskId(), userTasks);
        
        targetTask.changePriority(TaskPriority.fromValue(command.newTaskPriority()));
        vipPriorityPolice.execute(user, userTasks);

        targetTask.changeName(new TaskName(command.newTaskName()));
        targetTask.changeSchedule(createSchedule(command));
        
        saveTask.save(targetTask);

        return TaskDTO.fromTask(targetTask, command.userId());
    }

    private Task findTask(String taskId, List<Task> userTasks) {
        return userTasks.stream()
                .filter(t -> t.getId().getValue().equals(taskId))
                .findFirst()
                .orElseThrow(() -> TaskNotFound.taskNotFound(taskId));
    }

    private TaskSchedule createSchedule(ChangeTaskDataCommand command) {
        return new TaskSchedule(
            command.newTaskScheduledDate(),
            command.newTaskScheduledStartTime(),
            command.newTaskScheduledEndTime()
        );
    }
}
