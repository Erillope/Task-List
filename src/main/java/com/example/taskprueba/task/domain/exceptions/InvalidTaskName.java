package com.example.taskprueba.task.domain.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class InvalidTaskName extends SystemException {
    
    public InvalidTaskName(String message) {
        super(message);
    }

    public static InvalidTaskName invalidTaskName(String invalidName, String pattern) {
        return new InvalidTaskName("Invalid task name: " + invalidName + ". Valid pattern: " + pattern);
    }
    
}
