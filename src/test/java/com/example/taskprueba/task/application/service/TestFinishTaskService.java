package com.example.taskprueba.task.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.task.application.command.FinishTaskCommand;
import com.example.taskprueba.task.application.exceptions.TaskNotFound;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.values.TaskProgress;
import com.example.taskprueba.task.factories.TaskFactory;
import com.example.taskprueba.task.mocks.TaskRepositoryMock;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;
import com.example.taskprueba.user.mocks.UserRepositoryMock;

class TestFinishTaskService {

    private UserRepositoryMock userRepository;
    private TaskRepositoryMock taskRepository;
    private FinishTaskService service;
    User user;
    Task task;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryMock();
        taskRepository = new TaskRepositoryMock();
        service = new FinishTaskService(userRepository, taskRepository, taskRepository, userRepository);
        user = UserFactory.create();
        task = TaskFactory.createStarted();
    }


	@Test
	void shouldFinishTaskAndUpdateUserCounters() {
		user.addPendingTask(task);
		userRepository.save(user);
		taskRepository.save(task);

		service.execute(new FinishTaskCommand(user.getId().getValue(), task.getId().getValue()));

		Task updatedTask = taskRepository.getTaskById(task.getId().getValue()).orElseThrow();
		User updatedUser = userRepository.getUserById(user.getId().getValue()).orElseThrow();

		assertEquals(TaskProgress.COMPLETED, updatedTask.getProgress());
		assertEquals(0, updatedUser.getPendingTaskNumber().getValue());
		assertEquals(1, updatedUser.getFinishedTaskNumber().getValue());
	}

	@Test
	void shouldThrowWhenUserDoesNotExist() {
		taskRepository.save(task);

        FinishTaskCommand command = new FinishTaskCommand("missing-user-id", task.getId().getValue());
        assertThrows(UserNotFound.class, () -> service.execute(command));
	}

	@Test
	void shouldThrowWhenTaskDoesNotExist() {
		userRepository.save(user);

        FinishTaskCommand command = new FinishTaskCommand(user.getId().getValue(), "missing-task-id");
		assertThrows(TaskNotFound.class, () -> service.execute(command));
	}

}
