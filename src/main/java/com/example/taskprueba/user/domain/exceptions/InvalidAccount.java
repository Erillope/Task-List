package com.example.taskprueba.user.domain.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class InvalidAccount extends SystemException {
    
    public InvalidAccount(String message) {
        super(message);
    }

    public static InvalidAccount invalidAccount(String invalidAccount, String pattern) {
        return new InvalidAccount("The account " + invalidAccount + " is invalid. It must match the pattern: " + pattern);
    }
    
}
