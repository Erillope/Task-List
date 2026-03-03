package com.example.taskprueba.user.domain.values;

import org.mindrot.jbcrypt.BCrypt;

import com.example.taskprueba.user.domain.exceptions.InvalidPassword;

import lombok.Getter;

@Getter
public class Password {
    
    private static final String PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    
    private final String value;

    private Password(String value, boolean isEncrypted) {
        if (isEncrypted) {
            this.value = value;
        } else {
            validate(value);
            this.value = hash(value);
        }
    }

    private void validate(String value){
        if (value == null || value.isEmpty()) {
            throw InvalidPassword.invalidPassword(value, "not empty");
        }
        if (!value.matches(PATTERN)) {
            throw InvalidPassword.invalidPassword(value, PATTERN);
        }
    }

    private String hash(String value) {
        return BCrypt.hashpw(value, BCrypt.gensalt());
    }

    public boolean matches(String rawPassword) {
        return BCrypt.checkpw(rawPassword, this.value);
    }

    public static Password of(String value) {
        return new Password(value, false);
    }

    public static Password hashed(String value) {
        return new Password(value, true);
    }

}
