package com.example.taskprueba.task.domain.policy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.taskprueba.task.domain.exceptions.ReachedLimitStartTask;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.values.TaskProgress;
import com.example.taskprueba.user.domain.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartTaskPolicy {

    private final int maxInProgressTasks;
    
    public void execute(User user, List<Task> userTasks) {
        if (user.isVip()){
            return;
        }
        if (hasMaxInProgressTask(userTasks)){
            throw ReachedLimitStartTask.reachedLimit();
        }
    }

    private boolean hasMaxInProgressTask(List<Task> userTasks) {
        return userTasks.stream().filter(t -> t.getProgress() == TaskProgress.IN_PROGRESS).count() > maxInProgressTasks;
    }

}
