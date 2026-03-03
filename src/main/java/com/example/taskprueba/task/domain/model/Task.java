package com.example.taskprueba.task.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.taskprueba.task.domain.exceptions.InvalidTaskSchedule;
import com.example.taskprueba.task.domain.exceptions.TaskProgressException;
import com.example.taskprueba.task.domain.values.TaskCreationData;
import com.example.taskprueba.task.domain.values.TaskID;
import com.example.taskprueba.task.domain.values.TaskName;
import com.example.taskprueba.task.domain.values.TaskPriority;
import com.example.taskprueba.task.domain.values.TaskProgress;
import com.example.taskprueba.task.domain.values.TaskSchedule;
import com.example.taskprueba.task.domain.values.TaskSnapshot;
import com.example.taskprueba.user.domain.values.UserID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Task {
    
    private final TaskID id;
    private UserID userId;
    private TaskName name;
    private TaskPriority priority;
    private TaskSchedule schedule;
    private TaskProgress progress;

    public void changeName(TaskName newName) {
        this.name = newName;
    }

    public void changePriority(TaskPriority newPriority) {
        this.priority = newPriority;
    }

    public void changeSchedule(TaskSchedule newSchedule) {
        validateSchedule(newSchedule);
        this.schedule = newSchedule;
    }

    public void start() {
        if (this.progress != TaskProgress.NOT_STARTED) {
            throw TaskProgressException.alreadyInProgressOrCompleted();
        }
        this.progress = TaskProgress.IN_PROGRESS;
    }

    public void complete() {
        if (this.progress != TaskProgress.IN_PROGRESS) {
            throw TaskProgressException.notInProgress();
        }
        this.progress = TaskProgress.COMPLETED;
    }

    public boolean isPending() {
        return this.progress == TaskProgress.NOT_STARTED || this.progress == TaskProgress.IN_PROGRESS;
    }

    public void setUserId(UserID userId) {
        this.userId = userId;
    }

    private static void validateSchedule(TaskSchedule schedule) {
        if (schedule.getScheduledDate().isBefore(LocalDate.now())){
            throw InvalidTaskSchedule.scheduleDateIsBeforeCurrentDate();
        }
        if (schedule.getStartTime().isBefore(LocalTime.now()) && schedule.getScheduledDate().isEqual(LocalDate.now())) {
            throw InvalidTaskSchedule.scheduleStartTimeIsBeforeCurrentTime();
        }
    }

    public static Task create(TaskCreationData data) {
        validateSchedule(data.schedule());
        return Task.builder()
            .id(TaskID.uuid())
            .userId(null)
            .name(data.name())
            .priority(data.priority())
            .schedule(data.schedule())
            .progress(TaskProgress.NOT_STARTED)
            .build();
    }

    public static Task restore(TaskSnapshot snapshot) {
        return Task.builder()
            .id(snapshot.id())
            .userId(snapshot.userId())
            .name(snapshot.name())
            .priority(snapshot.priority())
            .schedule(snapshot.schedule())
            .progress(snapshot.progress())
            .build();
    }

}
