package com.example.taskprueba.task.factories;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.values.TaskCreationData;
import com.example.taskprueba.task.domain.values.TaskName;
import com.example.taskprueba.task.domain.values.TaskPriority;
import com.example.taskprueba.task.domain.values.TaskSchedule;
import com.example.taskprueba.user.domain.values.UserID;

public class TaskFactory {

    public static TaskSchedule validSchedule() {
        LocalTime now = LocalTime.now();
        LocalTime startTime = now.plusMinutes(1);
        LocalTime endTime = startTime.plusHours(1);

        return new TaskSchedule(LocalDate.now().plusDays(1), startTime, endTime);
    }

    public static TaskSchedule invalidSchedule() {
        return new TaskSchedule(
			LocalDate.now(),
			LocalTime.now().minusNanos(1),
			LocalTime.MAX
		);
    }

    public static Task create(String name, TaskPriority priority) {
        TaskCreationData data = new TaskCreationData(
                new TaskName(name),
                priority,
                validSchedule());
        return Task.create(data);
    }

    public static Task create() {
        return create("task_name", TaskPriority.NORMAL);
    }

    public static Task createWithUserId() {
        Task task = create();
        task.setUserId(UserID.uuid());
        return task;
    }

    public static Task createStarted(String name, TaskPriority priority) {
        Task task = create(name, priority);
        task.start();
        return task;
    }

    public static Task createStarted() {
        return createStarted("task_name", TaskPriority.NORMAL);
    }

    public static Task createCompleted(String name, TaskPriority priority) {
        Task task = create(name, priority);
        task.start();
        task.complete();
        return task;
    }

    public static Task createCompleted() {
        return createCompleted("task_name", TaskPriority.NORMAL);
    }

}
