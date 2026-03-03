package com.example.taskprueba.user.infrastructure.repository;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.example.taskprueba.user.application.exceptions.UserAlreadyRegistered;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;
import com.example.taskprueba.user.domain.persistence_ports.SaveUser;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository implements GetUser, SaveUser{
    
    private final UserJpaRepository userJpaRepository;

    @Override
    public boolean existsById(String id) {
        return userJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByAccount(String account) {
        return userJpaRepository.existsByAccount(account);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userJpaRepository.findById(id).map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> getUserByAccount(String account) {
        return userJpaRepository.findByAccount(account).map(UserEntityMapper::toDomain);
    }

    @Override
    public void save(User user) {
        UserEntity userEntity = UserEntityMapper.toEntity(user);
        try {
            userJpaRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw UserAlreadyRegistered.userAlreadyRegistered(user.getAccount().getValue());
        }
    }

}
