package com.example.taskprueba.user.domain.values;

import com.example.taskprueba.common.values.NonNegativeNumber;

import lombok.Builder;

@Builder
public record UserSnapshot(
    UserID id,
    UserName userName,
    Account account,
    Password password,
    boolean isVip,
    NonNegativeNumber pendingTaskNumber,
    NonNegativeNumber finishedTaskNumber
) {
}
