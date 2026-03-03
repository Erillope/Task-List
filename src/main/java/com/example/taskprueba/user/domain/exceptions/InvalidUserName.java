package com.example.taskprueba.user.domain.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class InvalidUserName extends SystemException {
    
    public InvalidUserName(String message) {
        super(message);
    }

    public static InvalidUserName invalidUserName(String invalidUserName, String pattern) {
        return new InvalidUserName("The username " + invalidUserName + " is invalid. It must match the pattern: " + pattern);
    }
    
}
