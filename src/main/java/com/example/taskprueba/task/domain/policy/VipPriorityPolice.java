package com.example.taskprueba.task.domain.policy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.values.TaskLimitation;
import com.example.taskprueba.user.domain.model.User;

@Component
public class VipPriorityPolice {
    
    private final List<TaskPriorityValidation> priorityLimitValidations;

    private final TaskLimitation taskLimitation;

    public VipPriorityPolice(TaskLimitation taskLimitation) {
        this.priorityLimitValidations = List.of(
            TaskPriorityValidation.inamovableCase(taskLimitation.inamovableTasksLimit().getValue()),
            TaskPriorityValidation.importantCase(taskLimitation.importantTasksLimit().getValue()),
            TaskPriorityValidation.urgentCase(taskLimitation.urgentTasksLimit().getValue())
        );
        this.taskLimitation = taskLimitation;
    }

    public void execute(User user, List<Task> userTasks) {
        if (user.isVip()) return;
        validateTasks(userTasks);
    }

    private void validateTasks(List<Task> userTasks) {
        int taskLimit = taskLimitation.tasksLimit().getValue();

        TaskPriorityValidation.defaultCase(taskLimit).validateTask(userTasks);
        
        for (TaskPriorityValidation priorityValidation : priorityLimitValidations) {
            priorityValidation.validateTask(userTasks);
        }
    }

}
