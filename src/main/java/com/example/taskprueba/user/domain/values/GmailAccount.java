package com.example.taskprueba.user.domain.values;

import com.example.taskprueba.user.domain.exceptions.InvalidAccount;

public class GmailAccount extends Account {
    private static final String PATTERN = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";

    public GmailAccount(String value) {
        super(value);
    }

    @Override
    protected void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw InvalidAccount.invalidAccount(value, "not empty");
        }
        if (!isGmailAccount(value)) {
            throw InvalidAccount.invalidAccount(value, PATTERN);
        }
    }

    public static boolean isGmailAccount(String value) {
        return value.matches(PATTERN);
    }
    
}
