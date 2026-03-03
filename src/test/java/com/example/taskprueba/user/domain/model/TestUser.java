package com.example.taskprueba.user.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.factories.TaskFactory;
import com.example.taskprueba.user.factories.UserFactory;

class TestUser {

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        user = UserFactory.create("test_user", "testuser@gmail.com", "abc12345");
        task = TaskFactory.create();
    }

	@Test
	void shouldCreateUserWithDefaults() {
		assertNotNull(user.getId());
		assertEquals("test_user", user.getUserName().getValue());
		assertEquals("testuser@gmail.com", user.getAccount().getValue());
		assertFalse(user.isVip());
		assertEquals(0, user.getPendingTaskNumber().getValue());
		assertEquals(0, user.getFinishedTaskNumber().getValue());
	}

	@Test
	void shouldReturnTrueWhenIsMeMatchesAccountAndPassword() {
		assertTrue(user.isMe("testuser@gmail.com", "abc12345"));
		assertFalse(user.isMe("other@gmail.com", "abc12345"));
	}

	@Test
	void shouldAddPendingTask() {
		user.addPendingTask(task);
		assertEquals(1, user.getPendingTaskNumber().getValue());
        assertEquals(user.getId(), task.getUserId());
	}

	@Test
	void shouldFinishTaskByMovingOneFromPendingToFinished() {
		user.addPendingTask(task);
		user.finishTask();

		assertEquals(0, user.getPendingTaskNumber().getValue());
		assertEquals(1, user.getFinishedTaskNumber().getValue());
	}

	@Test
	void shouldRemovePendingTask() {
		user.addPendingTask(task);
		user.removePendingTask(task);

		assertEquals(0, user.getPendingTaskNumber().getValue());
        assertNull(task.getUserId());
	}

	@Test
	void shouldUpgradeToVip() {
		user.upgradeToVip();
		assertTrue(user.isVip());
	}

	@Test
	void shouldRemoveToVip() {
		user.upgradeToVip();
		user.removeVip();

		assertFalse(user.isVip());
	}
}
