package com.example.taskprueba.task.domain.exceptions;

import com.example.taskprueba.common.exceptions.SystemException;

public class InvalidTaskSchedule extends SystemException {
    
    public InvalidTaskSchedule(String message) {
        super(message);
    }

    public static InvalidTaskSchedule scheduleDateIsBeforeCurrentDate() {
        return new InvalidTaskSchedule("The task schedule date cannot be before the current date");
    }

    public static InvalidTaskSchedule startTimeIsAfterEndTime() {
        return new InvalidTaskSchedule("The task start time cannot be after the end time");
    }
    
    public static InvalidTaskSchedule startTimeIsEqualToEndTime() {
        return new InvalidTaskSchedule("The task start time cannot be equal to the end time");
    }

    public static InvalidTaskSchedule scheduleStartTimeIsBeforeCurrentTime() {
        return new InvalidTaskSchedule("The task schedule start time cannot be before the current time");
    }
}
