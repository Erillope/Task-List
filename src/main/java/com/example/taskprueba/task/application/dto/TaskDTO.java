package com.example.taskprueba.task.application.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.taskprueba.task.domain.model.Task;

import lombok.Builder;

@Builder
public record TaskDTO(
    String id,
    String userId,
    String name,
    LocalDate scheduledDate,
    LocalTime startTime,
    LocalTime endTime,
    int priority,
    int progress
) {
    public static TaskDTO fromTask(Task task, String userId) {
        return new TaskDTO(
            task.getId().getValue(),
            userId,
            task.getName().getValue(),
            task.getSchedule().getScheduledDate(),
            task.getSchedule().getStartTime(),
            task.getSchedule().getEndTime(),
            task.getPriority().getValue(),
            task.getProgress().getValue()
        );
    }
}
