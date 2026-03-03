package com.example.taskprueba.task.domain.values;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.example.taskprueba.task.domain.exceptions.InvalidTaskName;

class TestTaskName {

	@Test
	void shouldCreateTaskNameWhenValueIsValid() {
		String value = "task_name_01";

		TaskName taskName = new TaskName(value);

		assertEquals(value, taskName.getValue());
	}

	@Test
	void shouldThrowWhenTaskNameHasInvalidFormat() {
		assertThrows(InvalidTaskName.class, () -> new TaskName("ab"));
	}

	@Test
	void shouldThrowWhenTaskNameIsEmpty() {
		assertThrows(InvalidTaskName.class, () -> new TaskName(""));
	}

	@Test
	void shouldThrowWhenTaskNameIsNull() {
		assertThrows(InvalidTaskName.class, () -> new TaskName(null));
	}
}
