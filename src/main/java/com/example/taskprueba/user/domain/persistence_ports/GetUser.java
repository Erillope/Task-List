package com.example.taskprueba.user.domain.persistence_ports;

import java.util.Optional;

import com.example.taskprueba.user.domain.model.User;

public interface GetUser {

    public boolean existsById(String id);

    public boolean existsByAccount(String account);
    
    public Optional<User> getUserById(String id);

    public Optional<User> getUserByAccount(String account);

}
