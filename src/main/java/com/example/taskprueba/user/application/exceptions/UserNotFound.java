package com.example.taskprueba.user.application.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class UserNotFound extends SystemException{
    
    private UserNotFound(String message) {
        super(message);
    }

    public static UserNotFound userNotFound(String account) {
        return new UserNotFound("The account " + account + " is not registered.");
    }
    
}
