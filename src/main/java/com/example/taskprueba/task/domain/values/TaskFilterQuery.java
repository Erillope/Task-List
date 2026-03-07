package com.example.taskprueba.task.domain.values;

import java.time.LocalDate;

import com.example.taskprueba.common.values.NonNegativeNumber;
import com.example.taskprueba.user.domain.values.UserID;

public record TaskFilterQuery(
    UserID userId,
    LocalDate scheduledDate,
    NonNegativeNumber page,
    NonNegativeNumber size
) {
    
}
