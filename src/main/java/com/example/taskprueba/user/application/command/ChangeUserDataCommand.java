package com.example.taskprueba.user.application.command;

import jakarta.validation.constraints.NotBlank;

public record ChangeUserDataCommand(
    @NotBlank String id,
    String newUserName,
    String newAccount
) {
    
}
