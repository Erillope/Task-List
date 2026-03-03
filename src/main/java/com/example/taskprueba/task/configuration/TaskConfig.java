package com.example.taskprueba.task.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.taskprueba.common.values.NonNegativeNumber;
import com.example.taskprueba.task.domain.policy.StartTaskPolicy;
import com.example.taskprueba.task.domain.policy.VipPriorityPolice;
import com.example.taskprueba.task.domain.values.TaskLimitation;

@Configuration
public class TaskConfig {
    
    public static final TaskLimitation DEFAULT_TASK_LIMITATION = TaskLimitation.builder()
        .inamovableTasksLimit(new NonNegativeNumber(1))
        .importantTasksLimit(new NonNegativeNumber(2))
        .urgentTasksLimit(new NonNegativeNumber(3))
        .tasksLimit(new NonNegativeNumber(10))
        .build();

    public static final int MAX_IN_PROGRESS_TASKS = 1;

    @Bean
    public VipPriorityPolice vipPriorityPolice() {
        return new VipPriorityPolice(DEFAULT_TASK_LIMITATION);
    }

    @Bean
    public StartTaskPolicy startTaskPolicy() {
        return new StartTaskPolicy(MAX_IN_PROGRESS_TASKS);
    }
    
}
