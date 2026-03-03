package com.example.taskprueba.common.exceptions;

public class NegativeNumber extends SystemException {
    
    public NegativeNumber(String message) {
        super(message);
    }

    public static NegativeNumber negative() {
        return new NegativeNumber("Negative numbers are not allowed.");
    }
    
}
