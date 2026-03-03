package com.example.taskprueba.task.domain.values;

import com.example.taskprueba.task.domain.exceptions.InvalidTaskName;

import lombok.Getter;

@Getter
public class TaskName {
    private static final String PATTERN = "^[a-zA-Z0-9._-]{3,}$";

    private final String value;

    public TaskName(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw InvalidTaskName.invalidTaskName(value, "not empty");
        }
        if (!value.matches(PATTERN)) {
            throw InvalidTaskName.invalidTaskName(value, PATTERN);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TaskName taskName = (TaskName) obj;
        return value.equals(taskName.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
