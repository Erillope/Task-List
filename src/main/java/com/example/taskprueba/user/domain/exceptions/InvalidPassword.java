package com.example.taskprueba.user.domain.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class InvalidPassword extends SystemException {
    
    public InvalidPassword(String message) {
        super(message);
    }

    public static InvalidPassword invalidPassword(String invalidPassword, String pattern) {
        return new InvalidPassword("The password " + invalidPassword + " is invalid. It must match the pattern: " + pattern);
    }
    
}
