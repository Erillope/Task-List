package com.example.taskprueba.task.domain.values;

import com.example.taskprueba.user.domain.values.UserID;

public record TaskSnapshot(
    TaskID id,
    UserID userId,
    TaskName name,
    TaskPriority priority,
    TaskSchedule schedule,
    TaskProgress progress
) {
    
}
