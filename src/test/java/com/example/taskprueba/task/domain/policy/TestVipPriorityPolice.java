package com.example.taskprueba.task.domain.policy;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.common.values.NonNegativeNumber;
import com.example.taskprueba.task.domain.exceptions.ReachedLimitTaskAssigment;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.values.TaskLimitation;
import com.example.taskprueba.task.domain.values.TaskPriority;
import com.example.taskprueba.task.factories.TaskFactory;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;

class TestVipPriorityPolice {

    User nonVipUser;
    User vipUser;
    VipPriorityPolice policy;

    @BeforeEach
    void setUp() {
        nonVipUser = UserFactory.create();
        vipUser = UserFactory.createVip();
        TaskLimitation limitation = new TaskLimitation(
            new NonNegativeNumber(1),
            new NonNegativeNumber(1),
            new NonNegativeNumber(1),
            new NonNegativeNumber(2)
        );
        policy = new VipPriorityPolice(limitation);
    }


	@Test
	void shouldNotValidateLimitsWhenUserIsVip() {

		List<Task> tasks = List.of(
			TaskFactory.create("task_name", TaskPriority.INAMOVABLE),
            TaskFactory.create("task_name", TaskPriority.INAMOVABLE),
            TaskFactory.create("task_name", TaskPriority.IMPORTANT)
		);

		assertDoesNotThrow(() -> policy.execute(vipUser, tasks));
	}

	@Test
	void shouldThrowWhenNonVipExceedsGeneralTaskLimit() {
		List<Task> tasks = List.of(
			TaskFactory.create("task_name", TaskPriority.NORMAL),
            TaskFactory.create("task_name", TaskPriority.SECONDARY),
			TaskFactory.create("task_name", TaskPriority.IMPORTANT)
		);

		assertThrows(ReachedLimitTaskAssigment.class, () -> policy.execute(nonVipUser, tasks));
	}

	@Test
	void shouldThrowWhenNonVipExceedsInamovableLimit() {
		List<Task> tasks = List.of(
			TaskFactory.create("task_name", TaskPriority.INAMOVABLE),
			TaskFactory.create("task_name", TaskPriority.INAMOVABLE)
		);

		assertThrows(ReachedLimitTaskAssigment.class, () -> policy.execute(nonVipUser, tasks));
	}

	@Test
	void shouldPassWhenNonVipIsWithinLimits() {
		List<Task> tasks = List.of();

		assertDoesNotThrow(() -> policy.execute(nonVipUser, tasks));
	}
}
