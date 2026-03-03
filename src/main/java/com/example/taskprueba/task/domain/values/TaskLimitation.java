package com.example.taskprueba.task.domain.values;

import com.example.taskprueba.common.values.NonNegativeNumber;

import lombok.Builder;

@Builder
public record TaskLimitation(
    NonNegativeNumber inamovableTasksLimit,
    NonNegativeNumber importantTasksLimit,
    NonNegativeNumber urgentTasksLimit,
    NonNegativeNumber tasksLimit
) {
    
}
