package com.example.taskprueba.task.application.command;

import jakarta.validation.constraints.NotBlank;

public record StartTaskCommand(
    @NotBlank String userId,
    @NotBlank String taskId
) {
    
}
