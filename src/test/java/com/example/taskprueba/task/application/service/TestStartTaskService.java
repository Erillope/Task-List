package com.example.taskprueba.task.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.task.application.command.StartTaskCommand;
import com.example.taskprueba.task.application.exceptions.TaskNotFound;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.policy.StartTaskPolicy;
import com.example.taskprueba.task.domain.values.TaskProgress;
import com.example.taskprueba.task.factories.TaskFactory;
import com.example.taskprueba.task.mocks.TaskRepositoryMock;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;
import com.example.taskprueba.user.mocks.UserRepositoryMock;

class TestStartTaskService {

	private UserRepositoryMock userRepository;
    private TaskRepositoryMock taskRepository;
    private StartTaskService service;
    User user;
    Task task;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryMock();
        taskRepository = new TaskRepositoryMock();
        StartTaskPolicy startTaskPolicy = new StartTaskPolicy(2);
        service = new StartTaskService(userRepository, taskRepository, taskRepository, startTaskPolicy);
        user = UserFactory.create();
        task = TaskFactory.create();
    }

	@Test
	void shouldStartTaskWhenUserAndTaskExistAndPolicyAllows() {
		userRepository.save(user);
		taskRepository.save(task);

		service.execute(new StartTaskCommand(user.getId().getValue(), task.getId().getValue()));

		Task updatedTask = taskRepository.getTaskById(task.getId().getValue()).orElseThrow();
		assertEquals(TaskProgress.IN_PROGRESS, updatedTask.getProgress());
	}

	@Test
	void shouldThrowWhenUserDoesNotExist() {
		taskRepository.save(task);

        StartTaskCommand command = new StartTaskCommand("missing-user-id", task.getId().getValue());
		assertThrows(UserNotFound.class, () -> service.execute(command));
	}

	@Test
	void shouldThrowWhenTaskDoesNotExist() {
		userRepository.save(user);

        StartTaskCommand command = new StartTaskCommand(user.getId().getValue(), "missing-task-id");
		assertThrows(TaskNotFound.class, () -> service.execute(command));
	}

}
