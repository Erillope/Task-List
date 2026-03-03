package com.example.taskprueba.task.domain.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class InvalidTaskPriority extends SystemException {
    
    public InvalidTaskPriority(String message) {
        super(message);
    }

    public static InvalidTaskPriority invalidTaskPriority(int priorityNumber, int minPriority, int maxPriority) {
        return new InvalidTaskPriority("Invalid task priority: " + priorityNumber + ". Valid range: " + minPriority + " to " + maxPriority);
    }

    public static InvalidTaskPriority invalidTaskPriority(int priorityNumber) {
        return new InvalidTaskPriority("Invalid task priority: " + priorityNumber + ".");
    }

}
