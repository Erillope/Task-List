package com.example.taskprueba.user.application.command;

import jakarta.validation.constraints.NotBlank;

public record ChangeUserPasswordCommand(
    @NotBlank String id,
    @NotBlank String newPassword
) {
    
}
