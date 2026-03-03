package com.example.taskprueba.task.infrastructure.web;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import com.example.taskprueba.task.application.command.AddTaskCommand;
import com.example.taskprueba.task.application.command.ChangeTaskDataCommand;
import com.example.taskprueba.task.application.command.FinishTaskCommand;
import com.example.taskprueba.task.application.command.StartTaskCommand;
import com.example.taskprueba.task.application.dto.TaskDTO;
import com.example.taskprueba.task.application.query.ListTasksQuery;

public class TaskApiCall {

    private final TestRestTemplate restTemplate;
    private final int port;
    private final String hostURL;

    public TaskApiCall(TestRestTemplate restTemplate, int port, String hostURL) {
        this.restTemplate = restTemplate;
        this.port = port;
        this.hostURL = hostURL;
        ensurePatchSupport();
    }

    private void ensurePatchSupport() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    public ResponseEntity<TaskDTO> create(AddTaskCommand addCommand) {
        return restTemplate.postForEntity(hostURL + ":" + port + "/tasks", addCommand, TaskDTO.class);
    }

    public ResponseEntity<TaskDTO> update(ChangeTaskDataCommand updateCommand) {
        return restTemplate.exchange(hostURL + ":" + port + "/tasks", HttpMethod.PUT, new HttpEntity<>(updateCommand),
                TaskDTO.class);
    }

    public ResponseEntity<TaskDTO> start(StartTaskCommand startCommand) {
        return restTemplate.exchange(hostURL + ":" + port + "/tasks/start", HttpMethod.PATCH,
                new HttpEntity<>(startCommand), TaskDTO.class);
    }

    public ResponseEntity<TaskDTO> finish(FinishTaskCommand finishCommand) {
        return restTemplate.exchange(hostURL + ":" + port + "/tasks/finish", HttpMethod.PATCH,
                new HttpEntity<>(finishCommand), TaskDTO.class);
    }

    public ResponseEntity<Void> remove(String taskId, String userId) {
        String url = hostURL + ":" + port + "/tasks?taskID=" + taskId + "&userID=" + userId;
        return restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }

    public ResponseEntity<TaskDTO[]> list(ListTasksQuery query) {
        String userId = query.userId();
        String scheduledDate = query.scheduledDate() != null ? query.scheduledDate().toString() : "";
        int page = query.page();
        int size = query.size();

        String url = hostURL + ":" + port + "/tasks?userID=" + userId + "&scheduledDate=" + scheduledDate + "&page="
                + page + "&size=" + size;
        return restTemplate.getForEntity(url, TaskDTO[].class);
    }

    public TaskDTO givenCreatedTask(String userId) {
        AddTaskCommand command = new AddTaskCommand(
                userId,
                "TaskTitle",
                0,
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
        return create(command).getBody();
    }

    public TaskDTO givenStartedTask(String userId) {
        TaskDTO createdTask = givenCreatedTask(userId);

        StartTaskCommand startCommand = new StartTaskCommand(userId, createdTask.id());
        return start(startCommand).getBody();
    }
}
