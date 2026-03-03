package com.example.taskprueba.user.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {

    boolean existsByAccount(String account);

    Optional<UserEntity> findByAccount(String account);
    
}