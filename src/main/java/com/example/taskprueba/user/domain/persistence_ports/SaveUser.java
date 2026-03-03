package com.example.taskprueba.user.domain.persistence_ports;

import com.example.taskprueba.user.domain.model.User;

public interface SaveUser {
    
    public void save(User user);

}
