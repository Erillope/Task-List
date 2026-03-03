package com.example.taskprueba.task.domain.values;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import com.example.taskprueba.task.domain.exceptions.InvalidTaskSchedule;

class TestTaskSchedule {

	@Test
	void shouldCreateTaskScheduleWhenTimesAreValid() {
		LocalDate date = LocalDate.now().plusDays(1);
		LocalTime startTime = LocalTime.of(9, 0);
		LocalTime endTime = LocalTime.of(10, 0);

		TaskSchedule schedule = new TaskSchedule(date, startTime, endTime);

		assertEquals(date, schedule.getScheduledDate());
		assertEquals(startTime, schedule.getStartTime());
		assertEquals(endTime, schedule.getEndTime());
	}

	@Test
	void shouldThrowWhenStartTimeIsAfterEndTime() {
		LocalDate date = LocalDate.now().plusDays(1);
		LocalTime startTime = LocalTime.of(11, 0);
		LocalTime endTime = LocalTime.of(10, 0);
		assertThrows(InvalidTaskSchedule.class, () -> new TaskSchedule(date, startTime, endTime));
	}

	@Test
	void shouldThrowWhenStartTimeIsEqualToEndTime() {
		LocalDate date = LocalDate.now().plusDays(1);
		LocalTime startTime = LocalTime.of(10, 0);
		LocalTime endTime = LocalTime.of(10, 0);
		assertThrows(InvalidTaskSchedule.class, () -> new TaskSchedule(date, startTime, endTime));
	}
}
