package com.example.taskprueba.task.domain.values;

public record TaskCreationData(
    TaskName name,
    TaskPriority priority,
    TaskSchedule schedule
) {
    
}
