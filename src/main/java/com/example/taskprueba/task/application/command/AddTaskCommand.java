package com.example.taskprueba.task.application.command;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddTaskCommand(
    @NotBlank String userId,
    @NotBlank String taskName,
    @NotNull int taskPriority,
    @NotNull LocalDate taskScheduledDate,
    @NotNull LocalTime taskScheduledStartTime,
    @NotNull LocalTime taskScheduledEndTime
) {
    
}
