package com.example.taskprueba.task.domain.values;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.taskprueba.task.domain.exceptions.InvalidTaskSchedule;

import lombok.Getter;

@Getter
public class TaskSchedule {

    private final LocalDate scheduledDate;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TaskSchedule(LocalDate scheduledDate, LocalTime startTime, LocalTime endTime) {
        this.scheduledDate = scheduledDate;
        this.startTime = startTime;
        this.endTime = endTime;
        validate();
    }

    private void validate() {
        if (startTime.isAfter(endTime)) {
            throw InvalidTaskSchedule.startTimeIsAfterEndTime();
        }
        if (startTime.equals(endTime)) {
            throw InvalidTaskSchedule.startTimeIsEqualToEndTime();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskSchedule that = (TaskSchedule) o;

        if (!scheduledDate.equals(that.scheduledDate)) return false;
        if (!startTime.equals(that.startTime)) return false;
        return endTime.equals(that.endTime);
    }

    @Override
    public int hashCode() {
        int result = scheduledDate.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        return result;
    }
    
}
