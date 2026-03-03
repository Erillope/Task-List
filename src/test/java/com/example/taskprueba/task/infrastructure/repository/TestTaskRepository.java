package com.example.taskprueba.task.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.taskprueba.common.values.NonNegativeNumber;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.values.TaskFilterQuery;
import com.example.taskprueba.task.domain.values.TaskSchedule;
import com.example.taskprueba.task.factories.TaskFactory;
import com.example.taskprueba.user.domain.values.UserID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Import(TaskRepository.class)
class TestTaskRepository {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskJpaRepository taskJpaRepository;

    private TaskEntity createTaskEntity() {
        Task task = TaskFactory.createWithUserId();
        return TaskEntityMapper.toEntity(task);
    }

	@Test
	void shouldSaveAndFindTaskById() {
		TaskEntity entity = createTaskEntity();
        taskJpaRepository.save(entity);

		Task saved = taskRepository.getTaskById(entity.getId()).orElseThrow();
		assertEquals(entity.getId(), saved.getId().getValue());
		assertEquals(entity.getName(), saved.getName().getValue());
		assertEquals(entity.getUserId(), saved.getUserId().getValue());
		assertEquals(entity.getPriority(), saved.getPriority().getValue());
		assertEquals(entity.getScheduledDate(), saved.getSchedule().getScheduledDate());
		assertEquals(entity.getStartTime(), saved.getSchedule().getStartTime());
		assertEquals(entity.getEndTime(), saved.getSchedule().getEndTime());
	}

	@Test
	void shouldCheckExistsById() {
		TaskEntity entity = createTaskEntity();
        taskJpaRepository.save(entity);

		assertTrue(taskRepository.existsById(entity.getId()));
		assertFalse(taskRepository.existsById("missing-id"));
	}

	@Test
	void shouldGetTasksByUserId() {
        Task taskA1 = TaskFactory.createWithUserId();
        Task taskA2 = TaskFactory.createWithUserId();
        taskA2.setUserId(taskA1.getUserId());

		taskJpaRepository.save(TaskEntityMapper.toEntity(taskA1));
        taskJpaRepository.save(TaskEntityMapper.toEntity(taskA2));
        taskJpaRepository.save(createTaskEntity());

		List<Task> tasks = taskRepository.getTasksByUserId(taskA1.getUserId().getValue());

		assertEquals(2, tasks.size());
		assertTrue(tasks.stream().allMatch(task -> task.getUserId().getValue().equals(taskA1.getUserId().getValue())));
	}

    private TaskSchedule prepareTasksForUserAndScheduleTest(UserID userId) {
        TaskSchedule scheduleMatch = new TaskSchedule(LocalDate.now().plusDays(3), LocalTime.of(14, 0), LocalTime.of(15, 0));
		TaskSchedule scheduleOther = new TaskSchedule(LocalDate.now().plusDays(4), LocalTime.of(14, 0), LocalTime.of(15, 0));

        Task taskA1 = TaskFactory.createWithUserId();
        Task taskA2 = TaskFactory.createWithUserId();
        Task taskA3 = TaskFactory.createWithUserId();
        taskA1.setUserId(userId);
        taskA2.setUserId(userId);
        taskA3.setUserId(userId);
        taskA1.changeSchedule(scheduleMatch);
        taskA2.changeSchedule(scheduleMatch);
        taskA3.changeSchedule(scheduleOther);

		taskJpaRepository.save(TaskEntityMapper.toEntity(taskA1));
		taskJpaRepository.save(TaskEntityMapper.toEntity(taskA2));
		taskJpaRepository.save(TaskEntityMapper.toEntity(taskA3));

        return scheduleMatch;
    }

	@Test
	void shouldFilterTasksByUserAndScheduleWithPagination() {
        UserID userId = UserID.uuid();
		TaskSchedule scheduleMatch = prepareTasksForUserAndScheduleTest(userId);

		TaskFilterQuery query = new TaskFilterQuery(
			userId,
			scheduleMatch,
			new NonNegativeNumber(0),
			new NonNegativeNumber(1)
		);

		List<Task> result = taskRepository.filterTasks(query);

		assertEquals(1, result.size());
		assertEquals(userId.getValue(), result.get(0).getUserId().getValue());
		assertEquals(scheduleMatch.getScheduledDate(), result.get(0).getSchedule().getScheduledDate());
		assertEquals(scheduleMatch.getStartTime(), result.get(0).getSchedule().getStartTime());
		assertEquals(scheduleMatch.getEndTime(), result.get(0).getSchedule().getEndTime());
	}

	@Test
	void shouldDeleteTaskById() {
        TaskEntity entity = createTaskEntity();
		taskJpaRepository.save(entity);

		taskRepository.delete(entity.getId());

		assertFalse(taskJpaRepository.existsById(entity.getId()));
	}
}
