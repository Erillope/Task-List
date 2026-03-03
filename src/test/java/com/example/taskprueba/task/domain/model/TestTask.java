package com.example.taskprueba.task.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.task.domain.exceptions.InvalidTaskSchedule;
import com.example.taskprueba.task.domain.exceptions.TaskProgressException;
import com.example.taskprueba.task.domain.values.TaskCreationData;
import com.example.taskprueba.task.domain.values.TaskName;
import com.example.taskprueba.task.domain.values.TaskPriority;
import com.example.taskprueba.task.domain.values.TaskProgress;
import com.example.taskprueba.task.domain.values.TaskSchedule;
import com.example.taskprueba.task.factories.TaskFactory;

class TestTask {

    private Task task;

    @BeforeEach
    void setUp() {
        task = TaskFactory.create("task_test", TaskPriority.NORMAL);
    }

	@Test
	void shouldCreateTaskWithDefaultProgress() {
		assertNotNull(task.getId());
		assertEquals("task_test", task.getName().getValue());
		assertEquals(TaskPriority.NORMAL, task.getPriority());
		assertEquals(TaskProgress.NOT_STARTED, task.getProgress());
        assertNull(task.getUserId());
	}

	@Test
	void shouldChangeSchedule() {
		TaskSchedule newSchedule = TaskFactory.validSchedule();
		task.changeSchedule(newSchedule);

		assertEquals(newSchedule, task.getSchedule());
	}

	@Test
	void shouldStartTask() {
		task.start();

		assertEquals(TaskProgress.IN_PROGRESS, task.getProgress());
	}

	@Test
	void shouldCompleteTaskWhenInProgress() {
		task.start();
		task.complete();

		assertEquals(TaskProgress.COMPLETED, task.getProgress());
	}

	@Test
	void shouldThrowWhenCompleteWithoutStarting() {
		assertThrows(TaskProgressException.class, task::complete);
	}

	@Test
	void shouldReturnPendingStatusBasedOnProgress() {
		assertTrue(task.isPending());

		task.start();
		assertTrue(task.isPending());

		task.complete();
		assertFalse(task.isPending());
	}

	@Test
	void shouldValidateScheduleOnCreateWhenDateIsBeforeToday() {
		TaskSchedule invalidSchedule = TaskFactory.invalidSchedule();

		TaskCreationData data = new TaskCreationData(
			new TaskName("task_test"),
			TaskPriority.NORMAL,
			invalidSchedule
		);

		assertThrows(InvalidTaskSchedule.class, () -> Task.create(data));
	}

	@Test
	void shouldValidateScheduleOnChangeScheduleWhenStartTimeIsBeforeNow() {
		TaskSchedule invalidSchedule = TaskFactory.invalidSchedule();

		assertThrows(InvalidTaskSchedule.class, () -> task.changeSchedule(invalidSchedule));
	}
}
