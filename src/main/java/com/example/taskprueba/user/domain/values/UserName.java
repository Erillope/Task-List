package com.example.taskprueba.user.domain.values;

import com.example.taskprueba.user.domain.exceptions.InvalidUserName;

import lombok.Getter;

@Getter
public class UserName {
    private static final String PATTERN = "^[a-zA-Z0-9._-]{3,}$";
    
    private final String value;

    public UserName(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw InvalidUserName.invalidUserName(value, "not empty");
        }
        if (!value.matches(PATTERN)) {
            throw InvalidUserName.invalidUserName(value, PATTERN);
        }
    }

}
