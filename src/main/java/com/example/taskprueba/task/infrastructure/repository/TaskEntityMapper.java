package com.example.taskprueba.task.infrastructure.repository;

import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.values.TaskID;
import com.example.taskprueba.task.domain.values.TaskName;
import com.example.taskprueba.task.domain.values.TaskPriority;
import com.example.taskprueba.task.domain.values.TaskProgress;
import com.example.taskprueba.task.domain.values.TaskSchedule;
import com.example.taskprueba.user.domain.values.UserID;

public class TaskEntityMapper {
    
    private TaskEntityMapper() {}

    public static TaskEntity toEntity(Task task) {
        return TaskEntity.builder()
            .id(task.getId().getValue())
            .userId(task.getUserId().getValue())
            .name(task.getName().getValue())
            .priority(task.getPriority().getValue())
            .scheduledDate(task.getSchedule().getScheduledDate())
            .startTime(task.getSchedule().getStartTime())
            .endTime(task.getSchedule().getEndTime())
            .progress(task.getProgress().getValue())
            .build();
    }

    public static Task toDomain(TaskEntity entity) {
        return Task.builder()
            .id(new TaskID(entity.getId()))
            .userId(new UserID(entity.getUserId()))
            .name(new TaskName(entity.getName()))
            .priority(TaskPriority.fromValue(entity.getPriority()))
            .schedule(new TaskSchedule(
                entity.getScheduledDate(),
                entity.getStartTime(),
                entity.getEndTime()
            ))
            .progress(TaskProgress.fromValue(entity.getProgress()))
            .build();
    }
}
