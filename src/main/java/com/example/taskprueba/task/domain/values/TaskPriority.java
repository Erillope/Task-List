package com.example.taskprueba.task.domain.values;

import com.example.taskprueba.task.domain.exceptions.InvalidTaskPriority;

public enum TaskPriority {
    INAMOVABLE(5),
    IMPORTANT(4),
    URGENT(3),
    NORMAL(2),
    SECONDARY(1),
    POSTPONABLE(0);

    private final int value;

    TaskPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TaskPriority fromValue(int value) {
        for (TaskPriority priority : values()) {
            if (priority.value == value) {
                return priority;
            }
        }
        throw InvalidTaskPriority.invalidTaskPriority(value, POSTPONABLE.value, INAMOVABLE.value);
    }
}
