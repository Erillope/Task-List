package com.example.taskprueba.task.application.command;

public record RemoveTaskCommand(
    String userId,
    String taskId
) {
    
}
