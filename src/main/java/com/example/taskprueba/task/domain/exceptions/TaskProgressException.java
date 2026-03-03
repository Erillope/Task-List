package com.example.taskprueba.task.domain.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class TaskProgressException extends SystemException {
    
    public TaskProgressException(String message) {
        super(message);
    }

    public static TaskProgressException alreadyInProgressOrCompleted() {
        return new TaskProgressException("La tarea ya ha sido iniciada o completada.");
    }

    public static TaskProgressException notInProgress() {
        return new TaskProgressException("La tarea debe estar en progreso para realizar esta acción.");
    }
    
}
