package com.example.taskprueba.task.domain.persistence_ports;

import com.example.taskprueba.task.domain.model.Task;

public interface SaveTask {
    
    public void save(Task task);
    
}
