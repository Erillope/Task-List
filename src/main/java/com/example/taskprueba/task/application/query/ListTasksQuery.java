package com.example.taskprueba.task.application.query;

import java.time.LocalDate;

public record ListTasksQuery(
    String userId,
    LocalDate scheduledDate,
    int page,
    int size
) {
    
}
