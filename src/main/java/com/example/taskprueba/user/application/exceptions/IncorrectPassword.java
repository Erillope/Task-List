package com.example.taskprueba.user.application.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class IncorrectPassword extends SystemException{
    
    private IncorrectPassword(String message) {
        super(message);
    }

    public static IncorrectPassword incorrectPassword() {
        return new IncorrectPassword("The password is incorrect.");
    }
    
}
