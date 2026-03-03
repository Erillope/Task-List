package com.example.taskprueba.task.application.command;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangeTaskDataCommand(
    @NotBlank String userId,
    @NotBlank String taskId,
    @NotBlank String newTaskName,
    @NotNull Integer newTaskPriority,
    @NotNull LocalDate newTaskScheduledDate,
    @NotNull LocalTime newTaskScheduledStartTime,
    @NotNull LocalTime newTaskScheduledEndTime
) {
    
}
