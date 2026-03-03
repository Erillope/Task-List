package com.example.taskprueba.task.domain.values;

import com.example.taskprueba.common.exceptions.SystemException;

public enum TaskProgress {
    NOT_STARTED(0),
    IN_PROGRESS(1),
    COMPLETED(2);

    private final int value;

    TaskProgress(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TaskProgress fromValue(int value) {
        for (TaskProgress progress : TaskProgress.values()) {
            if (progress.getValue() == value) {
                return progress;
            }
        }
        throw new SystemException("Invalid TaskProgress value: " + value);
    }
}
