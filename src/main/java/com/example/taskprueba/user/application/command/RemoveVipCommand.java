package com.example.taskprueba.user.application.command;

import jakarta.validation.constraints.NotBlank;

public record RemoveVipCommand(
    @NotBlank String id
) {
    
}
