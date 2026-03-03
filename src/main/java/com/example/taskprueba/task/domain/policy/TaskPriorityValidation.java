package com.example.taskprueba.task.domain.policy;

import java.util.List;

import com.example.taskprueba.task.domain.exceptions.ReachedLimitTaskAssigment;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.values.TaskPriority;

public interface TaskPriorityValidation {

    public void validateTask(List<Task> userTasks);

    public static TaskPriorityValidation defaultCase(int limit) {
        return userTasks -> {
            int pendingTasks = userTasks.size();
            if (pendingTasks > limit) {
                throw ReachedLimitTaskAssigment.reachedLimit();
            }
        };
    }

    public static TaskPriorityValidation inamovableCase(int limit) {
        return userTasks -> {
            int inamovableTasks = (int) userTasks.stream()
                    .filter(t -> t.getPriority() == TaskPriority.INAMOVABLE)
                    .count();
            if (inamovableTasks > limit) throw ReachedLimitTaskAssigment.reachedLimit(TaskPriority.INAMOVABLE);
        };
    }

    public static TaskPriorityValidation importantCase(int limit) {
        return userTasks -> {
            int importantTasks = (int) userTasks.stream()
                    .filter(t -> t.getPriority() == TaskPriority.IMPORTANT)
                    .count();
            if (importantTasks > limit) throw ReachedLimitTaskAssigment.reachedLimit(TaskPriority.IMPORTANT);
        };
    }

    public static TaskPriorityValidation urgentCase(int limit) {
        return userTasks -> {
            int urgentTasks = (int) userTasks.stream()
                    .filter(t -> t.getPriority() == TaskPriority.URGENT)
                    .count();
            if (urgentTasks > limit) throw ReachedLimitTaskAssigment.reachedLimit(TaskPriority.URGENT);
        };
    }

}
