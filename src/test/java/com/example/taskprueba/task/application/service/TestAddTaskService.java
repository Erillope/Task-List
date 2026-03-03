package com.example.taskprueba.task.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.common.values.NonNegativeNumber;
import com.example.taskprueba.task.application.command.AddTaskCommand;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.policy.VipPriorityPolice;
import com.example.taskprueba.task.domain.values.TaskLimitation;
import com.example.taskprueba.task.mocks.TaskRepositoryMock;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;
import com.example.taskprueba.user.mocks.UserRepositoryMock;

class TestAddTaskService {

    private UserRepositoryMock userRepository;
    private TaskRepositoryMock taskRepository;
    private AddTaskService service;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryMock();
        taskRepository = new TaskRepositoryMock();
        VipPriorityPolice vipPriorityPolice = new VipPriorityPolice(new TaskLimitation(
            new NonNegativeNumber(1),
            new NonNegativeNumber(1),
            new NonNegativeNumber(1),
            new NonNegativeNumber(2)
        ));
        service = new AddTaskService(userRepository, taskRepository, taskRepository, userRepository, vipPriorityPolice);
    }

	private AddTaskCommand commandFor(String userId) {
		return new AddTaskCommand(
			userId,
			"task_test",
			2,
			LocalDate.now().plusDays(1),
			LocalTime.of(10, 0),
			LocalTime.of(11, 0)
		);
	}

	@Test
	void shouldAddTaskAndIncreasePendingCounter() {
		User user = UserFactory.create();
		userRepository.save(user);

		service.execute(commandFor(user.getId().getValue()));

		assertEquals(1, taskRepository.size());
		assertEquals(1, userRepository.getUserById(user.getId().getValue()).orElseThrow().getPendingTaskNumber().getValue());

		Task savedTask = taskRepository.getTasksByUserId(user.getId().getValue()).get(0);
		assertEquals("task_test", savedTask.getName().getValue());
	}

	@Test
	void shouldThrowWhenUserDoesNotExist() {
		AddTaskCommand command = commandFor("missing-user-id");
		assertThrows(UserNotFound.class, () -> service.execute(command));
	}

}
