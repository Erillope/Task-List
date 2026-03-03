package com.example.taskprueba.user.domain.values;

import com.example.taskprueba.user.domain.exceptions.InvalidAccount;

public class PhoneNumberAccount extends Account {
    private static final String PATTERN = "^\\+?[1-9]\\d{1,14}$";

    public PhoneNumberAccount(String value) {
        super(value);
    }

    @Override
    protected void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw InvalidAccount.invalidAccount(value, "not empty");
        }
        if (!isPhoneNumberAccount(value)) {
            throw InvalidAccount.invalidAccount(value, PATTERN);
        }
    }

    public static boolean isPhoneNumberAccount(String value) {
        return value.matches(PATTERN);
    }
    
}
