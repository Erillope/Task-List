package com.example.taskprueba.task.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.task.application.dto.TaskDTO;
import com.example.taskprueba.task.application.query.ListTasksQuery;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.factories.TaskFactory;
import com.example.taskprueba.task.mocks.TaskRepositoryMock;

class TestListTaskService {

	private TaskRepositoryMock repository;
    private ListTaskService service;

    @BeforeEach
    void setUp() {
        repository = new TaskRepositoryMock();
        service = new ListTaskService(repository);
    }

	@Test
	void shouldListTasksAndMapAllFieldsToDTO() {
		String userId = "user-123";
		Task task = TaskFactory.create();
		repository.save(task);

		List<TaskDTO> result = service.execute(new ListTasksQuery(userId, null, 0, 10));

		assertEquals(1, result.size());
		TaskDTO dto = result.get(0);
		assertEquals(task.getId().getValue(), dto.id());
		assertEquals(userId, dto.userId());
		assertEquals(task.getName().getValue(), dto.name());
		assertEquals(task.getSchedule().getScheduledDate(), dto.scheduledDate());
		assertEquals(task.getSchedule().getStartTime(), dto.startTime());
		assertEquals(task.getSchedule().getEndTime(), dto.endTime());
		assertEquals(task.getPriority().getValue(), dto.priority());
		assertEquals(task.getProgress().getValue(), dto.progress());
	}

}
