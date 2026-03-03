package com.example.taskprueba.user.application.command;

import jakarta.validation.constraints.NotBlank;

public record UpgradeVipCommand(
    @NotBlank String id
) {
    
}
