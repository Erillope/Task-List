package com.example.taskprueba.user.application.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class UserAlreadyRegistered extends SystemException{
    
    private UserAlreadyRegistered(String message) {
        super(message);
    }

    public static UserAlreadyRegistered userAlreadyRegistered(String account) {
        return new UserAlreadyRegistered("The account " + account + " is already registered.");
    }
    
}
