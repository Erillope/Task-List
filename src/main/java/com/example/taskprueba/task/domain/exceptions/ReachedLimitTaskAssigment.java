package com.example.taskprueba.task.domain.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;
import com.example.taskprueba.task.domain.values.TaskPriority;

public class ReachedLimitTaskAssigment extends SystemException {
    public ReachedLimitTaskAssigment(String message) {
        super(message);
    }

    public static ReachedLimitTaskAssigment reachedLimit(TaskPriority priority) {
        return new ReachedLimitTaskAssigment("User has reached the maximum number of " + priority + " tasks.");
    }

    public static ReachedLimitTaskAssigment reachedLimit() {
        return new ReachedLimitTaskAssigment("User has reached the maximum number of tasks.");
    }
    
}
