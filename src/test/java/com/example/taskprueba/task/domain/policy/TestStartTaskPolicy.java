package com.example.taskprueba.task.domain.policy;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.task.domain.exceptions.ReachedLimitStartTask;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.factories.TaskFactory;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;

class TestStartTaskPolicy {
    private User nonVipUser;
    private User vipUser;
    private StartTaskPolicy policy;

    @BeforeEach
    void setUp() {
        nonVipUser = UserFactory.create();
        vipUser = UserFactory.createVip();
        policy = new StartTaskPolicy(1);
    }

	@Test
	void shouldNotValidateLimitWhenUserIsVip() {
		List<Task> tasks = List.of(
			TaskFactory.createStarted(),
			TaskFactory.createStarted()
		);

		assertDoesNotThrow(() -> policy.execute(vipUser, tasks));
	}

	@Test
	void shouldThrowWhenNonVipReachedInProgressLimit() {
		List<Task> tasks = List.of(
			TaskFactory.createStarted(),
			TaskFactory.createStarted()
		);

		assertThrows(ReachedLimitStartTask.class, () -> policy.execute(nonVipUser, tasks));
	}

	@Test
	void shouldPassWhenNonVipIsBelowInProgressLimit() {
		List<Task> tasks = List.of(
			TaskFactory.createStarted(),
			TaskFactory.createCompleted()
		);

		assertDoesNotThrow(() -> policy.execute(nonVipUser, tasks));
	}
}
