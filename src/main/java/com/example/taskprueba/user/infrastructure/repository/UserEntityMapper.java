package com.example.taskprueba.user.infrastructure.repository;

import com.example.taskprueba.common.values.NonNegativeNumber;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.values.Account;
import com.example.taskprueba.user.domain.values.Password;
import com.example.taskprueba.user.domain.values.UserID;
import com.example.taskprueba.user.domain.values.UserName;
import com.example.taskprueba.user.domain.values.UserSnapshot;

public class UserEntityMapper {
    
    private UserEntityMapper() {}

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId().getValue())
                .userName(user.getUserName().getValue())
                .account(user.getAccount().getValue())
                .password(user.getPassword().getValue())
                .isVip(user.isVip())
                .pendingTaskNumber(user.getPendingTaskNumber().getValue())
                .finishedTaskNumber(user.getFinishedTaskNumber().getValue())
                .build();
    }

    public static User toDomain(UserEntity entity) {
        UserSnapshot snapshot = UserSnapshot.builder()
                .id(new UserID(entity.getId()))
                .userName(new UserName(entity.getUserName()))
                .account(Account.of(entity.getAccount()))
                .password(Password.hashed(entity.getPassword()))
                .isVip(entity.isVip())
                .pendingTaskNumber(new NonNegativeNumber(entity.getPendingTaskNumber()))
                .finishedTaskNumber(new NonNegativeNumber(entity.getFinishedTaskNumber()))
                .build();
        return User.restore(snapshot);
    }

}
