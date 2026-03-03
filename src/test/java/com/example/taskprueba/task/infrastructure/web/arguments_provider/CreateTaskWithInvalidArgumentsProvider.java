package com.example.taskprueba.task.infrastructure.web.arguments_provider;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class CreateTaskWithInvalidArgumentsProvider implements ArgumentsProvider {

    private static LocalDate futureDate = LocalDate.now().plusDays(1);
    private static LocalTime validStartTime = LocalTime.of(9, 0);
    private static LocalTime validEndTime = LocalTime.of(10, 0);

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                nullArguments(),
                invalidTaskNameArguments(),
                invalidTaskPriorityArguments(),
                startTimeAfterEndTimeArguments(),
                startTimeEqualToEndTimeArguments(),
                scheduleDateBeforeCurrentDateArguments(),
                scheduleStartTimeBeforeCurrentTimeArguments()
        );
    }

    private Arguments nullArguments() {
        return Arguments.of("user2@gmail.com", null, -1, null, null, null);
    }

    private Arguments invalidTaskNameArguments() {
        return Arguments.of("user3@gmail.com", "T", 0, futureDate, validStartTime, validEndTime);
    }

    private Arguments invalidTaskPriorityArguments() {
        return Arguments.of("user4@gmail.com", "TaskTitle", -1, futureDate, validStartTime, validEndTime);
    }

    private Arguments startTimeAfterEndTimeArguments() {
        return Arguments.of("user5@gmail.com", "TaskTitle", 0, futureDate, LocalTime.of(8, 0), LocalTime.of(5, 0));
    }

    private Arguments startTimeEqualToEndTimeArguments() {
        return Arguments.of("user6@gmail.com", "TaskTitle", 0, futureDate, LocalTime.of(9, 0), LocalTime.of(9, 0));
    }

    private Arguments scheduleDateBeforeCurrentDateArguments() {
        return Arguments.of("user7@gmail.com", "TaskTitle", 0, LocalDate.now().minusDays(1), validStartTime, validEndTime);
    }

    private Arguments scheduleStartTimeBeforeCurrentTimeArguments() {
        return Arguments.of("user8@gmail.com", "TaskTitle", 0, LocalDate.now(), LocalTime.now().minusHours(1), LocalTime.now());
    }

}
