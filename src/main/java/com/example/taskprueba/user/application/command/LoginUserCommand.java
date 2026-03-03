package com.example.taskprueba.user.application.command;

import jakarta.validation.constraints.NotBlank;

public record LoginUserCommand(
    @NotBlank String account,
    @NotBlank String password
) {
    
}
