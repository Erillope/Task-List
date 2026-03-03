package com.example.taskprueba.user.domain.values;

public record UserCreationData(
    UserName userName,
    Account account,
    Password password
) {
}