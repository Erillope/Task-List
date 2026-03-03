package com.example.taskprueba.task.domain.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class ReachedLimitStartTask extends SystemException {
    
    public ReachedLimitStartTask(String message) {
        super(message);
    }

    public static ReachedLimitStartTask reachedLimit() {
        return new ReachedLimitStartTask("El usuario ha alcanzado el límite de tareas en progreso.");
    }
    
}
