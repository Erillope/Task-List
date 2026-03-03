package com.example.taskprueba.user.application.dto;

import com.example.taskprueba.user.domain.model.User;

import lombok.Builder;

@Builder
public record UserDTO(
    String id,
    String name,
    String account,
    boolean isVip,
    int pendingTaskNumber,
    int finishedTaskNumber
) {
    public static UserDTO from(User user) {
        return new UserDTO(
            user.getId().getValue(),
            user.getUserName().getValue(),
            user.getAccount().getValue(),
            user.isVip(),
            user.getPendingTaskNumber().getValue(),
            user.getFinishedTaskNumber().getValue()
        );
    }
}
