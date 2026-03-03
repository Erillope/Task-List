package com.example.taskprueba.task.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.task.application.command.RemoveTaskCommand;
import com.example.taskprueba.task.application.exceptions.TaskNotFound;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.factories.TaskFactory;
import com.example.taskprueba.task.mocks.TaskRepositoryMock;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;
import com.example.taskprueba.user.mocks.UserRepositoryMock;

class TestRemoveTaskService {

    private UserRepositoryMock userRepository;
    private TaskRepositoryMock taskRepository;
    private RemoveTaskService service;
    User user;
    Task pendingTask;
    Task completedTask;

	@BeforeEach
    void setUp() {
        userRepository = new UserRepositoryMock();
        taskRepository = new TaskRepositoryMock();
        service = new RemoveTaskService(userRepository, userRepository, taskRepository, taskRepository);
        user = UserFactory.create();
        pendingTask = TaskFactory.create();
        completedTask = TaskFactory.createCompleted();
    }

	@Test
	void shouldRemovePendingTaskAndDecreasePendingCounter() {
		user.addPendingTask(pendingTask);
		userRepository.save(user);
		taskRepository.save(pendingTask);

		service.execute(new RemoveTaskCommand(user.getId().getValue(), pendingTask.getId().getValue()));

		User updatedUser = userRepository.getUserById(user.getId().getValue()).orElseThrow();
		assertEquals(0, updatedUser.getPendingTaskNumber().getValue());
		assertFalse(taskRepository.existsById(pendingTask.getId().getValue()));
	}

	@Test
	void shouldRemoveCompletedTaskWithoutChangingPendingCounter() {
		user.addPendingTask(completedTask);
		userRepository.save(user);
		taskRepository.save(completedTask);

		service.execute(new RemoveTaskCommand(user.getId().getValue(), completedTask.getId().getValue()));

		User updatedUser = userRepository.getUserById(user.getId().getValue()).orElseThrow();
		assertEquals(1, updatedUser.getPendingTaskNumber().getValue());
		assertFalse(taskRepository.existsById(completedTask.getId().getValue()));
	}

	@Test
	void shouldThrowWhenUserDoesNotExist() {
		taskRepository.save(pendingTask);

        RemoveTaskCommand command = new RemoveTaskCommand("missing-user-id", pendingTask.getId().getValue());
        assertThrows(UserNotFound.class, () -> service.execute(command));
	}

	@Test
	void shouldThrowWhenTaskDoesNotExist() {
		userRepository.save(user);

        RemoveTaskCommand command = new RemoveTaskCommand(user.getId().getValue(), "missing-task-id");
		assertThrows(TaskNotFound.class, () -> service.execute(command));
	}
}
