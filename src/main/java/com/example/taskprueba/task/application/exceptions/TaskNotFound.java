package com.example.taskprueba.task.application.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class TaskNotFound extends SystemException{
    
    public TaskNotFound(String taskId) {
        super("Task with id " + taskId + " not found");
    }

    public static TaskNotFound taskNotFound(String taskId) {
        return new TaskNotFound(taskId);
    }
    
}
