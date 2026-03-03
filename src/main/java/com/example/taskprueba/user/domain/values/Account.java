package com.example.taskprueba.user.domain.values;

import com.example.taskprueba.user.domain.exceptions.InvalidAccount;

import lombok.Getter;

@Getter
public abstract class Account {
    
    protected final String value;

    protected Account(String value) {
        validate(value);
        this.value = value;
    }

    protected abstract void validate(String value);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account account = (Account) obj;
        return value.equals(account.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static Account of(String value) {
        if (GmailAccount.isGmailAccount(value)) return new GmailAccount(value);
        
        if (PhoneNumberAccount.isPhoneNumberAccount(value)) return new PhoneNumberAccount(value);
        
        throw InvalidAccount.invalidAccount(value, "Gmail or PhoneNumber");
    }

}
