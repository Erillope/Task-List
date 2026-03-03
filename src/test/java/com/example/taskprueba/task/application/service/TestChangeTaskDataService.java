package com.example.taskprueba.task.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.common.values.NonNegativeNumber;
import com.example.taskprueba.task.application.command.ChangeTaskDataCommand;
import com.example.taskprueba.task.application.exceptions.TaskNotFound;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.policy.VipPriorityPolice;
import com.example.taskprueba.task.domain.values.TaskLimitation;
import com.example.taskprueba.task.domain.values.TaskPriority;
import com.example.taskprueba.task.factories.TaskFactory;
import com.example.taskprueba.task.mocks.TaskRepositoryMock;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;
import com.example.taskprueba.user.mocks.UserRepositoryMock;

class TestChangeTaskDataService {

    private UserRepositoryMock userRepository;
    private TaskRepositoryMock taskRepository;
    private ChangeTaskDataService service;
    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryMock();
        taskRepository = new TaskRepositoryMock();
        VipPriorityPolice vipPriorityPolice = new VipPriorityPolice(new TaskLimitation(
            new NonNegativeNumber(10),
            new NonNegativeNumber(10),
            new NonNegativeNumber(10),
            new NonNegativeNumber(10)
        ));
        service = new ChangeTaskDataService(userRepository, taskRepository, taskRepository, vipPriorityPolice);
        user = UserFactory.create();
        task = TaskFactory.create();
    }

	private ChangeTaskDataCommand commandFor(String userId, String taskId) {
		return new ChangeTaskDataCommand(
			userId,
			taskId,
			"task_updated",
			TaskPriority.IMPORTANT.getValue(),
			LocalDate.now().plusDays(2),
			LocalTime.of(11, 0),
			LocalTime.of(12, 0)
		);
	}

	@Test
	void shouldChangeTaskDataWhenUserAndTaskExist() {
		userRepository.save(user);
		taskRepository.save(task);

		service.execute(commandFor(user.getId().getValue(), task.getId().getValue()));

		Task updatedTask = taskRepository.getTaskById(task.getId().getValue()).orElseThrow();
		assertEquals("task_updated", updatedTask.getName().getValue());
		assertEquals(TaskPriority.IMPORTANT, updatedTask.getPriority());
		assertEquals(LocalDate.now().plusDays(2), updatedTask.getSchedule().getScheduledDate());
		assertEquals(LocalTime.of(11, 0), updatedTask.getSchedule().getStartTime());
		assertEquals(LocalTime.of(12, 0), updatedTask.getSchedule().getEndTime());
	}

	@Test
	void shouldThrowWhenUserDoesNotExist() {
		taskRepository.save(task);

        ChangeTaskDataCommand command = commandFor("missing-user-id", task.getId().getValue());
        assertThrows(UserNotFound.class, () -> service.execute(command));
	}

	@Test
	void shouldThrowWhenTaskDoesNotExist() {
		userRepository.save(user);

        ChangeTaskDataCommand command = commandFor(user.getId().getValue(), "missing-task-id");
		assertThrows(TaskNotFound.class, () -> service.execute(command));
	}

}
