package com.example.taskprueba.common.values;

import com.example.taskprueba.common.exceptions.NegativeNumber;

import lombok.Getter;

@Getter
public class NonNegativeNumber {
    private final int value;

    public NonNegativeNumber(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 0) {
            throw NegativeNumber.negative();
        }
    }

    public static NonNegativeNumber zero() {
        return new NonNegativeNumber(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NonNegativeNumber that = (NonNegativeNumber) obj;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}
