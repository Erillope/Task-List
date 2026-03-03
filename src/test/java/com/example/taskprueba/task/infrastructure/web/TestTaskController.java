package com.example.taskprueba.task.infrastructure.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.taskprueba.task.application.command.AddTaskCommand;
import com.example.taskprueba.task.application.command.ChangeTaskDataCommand;
import com.example.taskprueba.task.application.command.FinishTaskCommand;
import com.example.taskprueba.task.application.command.StartTaskCommand;
import com.example.taskprueba.task.application.dto.TaskDTO;
import com.example.taskprueba.task.application.query.ListTasksQuery;
import com.example.taskprueba.task.configuration.TaskConfig;
import com.example.taskprueba.task.infrastructure.web.arguments_provider.CreateTaskWithInvalidArgumentsProvider;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.infrastructure.web.UserApiCall;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestTaskController {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private TaskApiCall taskApiCall;
    private UserApiCall userApiCall;

    @BeforeEach
    void setup() {
        taskApiCall = new TaskApiCall(restTemplate, port, "http://localhost");
        userApiCall = new UserApiCall(restTemplate, port, "http://localhost");
    }

    private TaskDTO expectedTaskDTO(AddTaskCommand command, String taskId) {
        return TaskDTO.builder()
                .id(taskId)
                .userId(command.userId())
                .name(command.taskName())
                .priority(command.taskPriority())
                .scheduledDate(command.taskScheduledDate())
                .startTime(command.taskScheduledStartTime())
                .endTime(command.taskScheduledEndTime())
                .progress(0)
                .build();
    }

    private TaskDTO expectedTaskDTO(ChangeTaskDataCommand command, TaskDTO originalTask) {
        return TaskDTO.builder()
                .id(originalTask.id())
                .userId(command.userId())
                .name(command.newTaskName() != null ? command.newTaskName() : originalTask.name())
                .priority(command.newTaskPriority() != null ? command.newTaskPriority() : originalTask.priority())
                .scheduledDate(command.newTaskScheduledDate() != null ? command.newTaskScheduledDate() : originalTask.scheduledDate())
                .startTime(command.newTaskScheduledStartTime() != null ? command.newTaskScheduledStartTime() : originalTask.startTime())
                .endTime(command.newTaskScheduledEndTime() != null ? command.newTaskScheduledEndTime() : originalTask.endTime())
                .progress(originalTask.progress())
                .build();
    }
    

    @Test
    void shouldCreateTaskSuccessfully() {
        UserDTO registeredUser = userApiCall.givenRegisteredUser("user@gmail.com", "abc12345", "username");

        AddTaskCommand command = new AddTaskCommand(
            registeredUser.id(),
            "TaskTitle",
            0,
            LocalDate.now().plusDays(1),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0)
        );
        ResponseEntity<TaskDTO> response = taskApiCall.create(command);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        TaskDTO createdTask = response.getBody();
        TaskDTO expectedTaskDTO = expectedTaskDTO(command, createdTask.id());

        assertEquals(expectedTaskDTO, createdTask);

    }

    @ParameterizedTest
    @ArgumentsSource(CreateTaskWithInvalidArgumentsProvider.class)
    void shouldReturnBadRequestWhenCreateTask(
        String email,
        String taskName,
        int priority,
        LocalDate scheduledDate,
        LocalTime startTime,
        LocalTime endTime
    ) {
        UserDTO registeredUser = userApiCall.givenRegisteredUser(email, "abc12345", "username");
        AddTaskCommand command = new AddTaskCommand(
            registeredUser.id(),
            taskName,
            priority,
            scheduledDate,
            startTime,
            endTime
        );
        ResponseEntity<TaskDTO> response = taskApiCall.create(command);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenNonVipUserReachesMaxTasks() {
        UserDTO registeredUser = userApiCall.givenRegisteredUser("nonVipUser.@gmail.com", "abc12345", "username");
        int maxTasks = TaskConfig.DEFAULT_TASK_LIMITATION.tasksLimit().getValue();

        for (int i = 0; i < maxTasks; i++) {
            TaskDTO createdTask = taskApiCall.givenCreatedTask(registeredUser.id());
            assertInstanceOf(TaskDTO.class, createdTask);
        }

        AddTaskCommand command = new AddTaskCommand(
            registeredUser.id(),
            "Another Task",
            0,
            LocalDate.now().plusDays(1),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0)
        );

        ResponseEntity<TaskDTO> response = taskApiCall.create(command);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        UserDTO registeredUser = userApiCall.givenRegisteredUser("registeredUser@gmail.com", "abc12345", "username");
        TaskDTO createdTask = taskApiCall.givenCreatedTask(registeredUser.id());
        ChangeTaskDataCommand updateCommand = new ChangeTaskDataCommand(
            registeredUser.id(),
            createdTask.id(),
            "newTitle",
            1,
            LocalDate.now().plusDays(2),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0)
        );
        ResponseEntity<TaskDTO> response = taskApiCall.update(updateCommand);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        TaskDTO updatedTask = response.getBody();
        TaskDTO expectedTaskDTO = expectedTaskDTO(updateCommand, createdTask);

        assertEquals(expectedTaskDTO, updatedTask);
    }

    @Test
    void shouldStartTaskSuccessfully() {
        UserDTO registeredUser = userApiCall.givenRegisteredUser("testUser@gmail.com", "abc12345", "username");
        TaskDTO createdTask = taskApiCall.givenCreatedTask(registeredUser.id());

        StartTaskCommand startCommand = new StartTaskCommand(registeredUser.id(), createdTask.id());
        ResponseEntity<TaskDTO> response = taskApiCall.start(startCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        TaskDTO startedTask = response.getBody();
        assertEquals(1, startedTask.progress());
    }

    @Test
    void shouldReturnBadRequestWhenNonVipUserReachesMaxInProgressTasks() {
        UserDTO registeredUser = userApiCall.givenRegisteredUser("nonVipUser2@gmail.com", "abc12345", "username");

        for (int i = 0; i < TaskConfig.MAX_IN_PROGRESS_TASKS; i++) {
            TaskDTO createdTask = taskApiCall.givenCreatedTask(registeredUser.id());
            StartTaskCommand startCommand = new StartTaskCommand(registeredUser.id(), createdTask.id());

            ResponseEntity<TaskDTO> response = taskApiCall.start(startCommand);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        TaskDTO anotherTask = taskApiCall.givenCreatedTask(registeredUser.id());
        StartTaskCommand startCommand = new StartTaskCommand(registeredUser.id(), anotherTask.id());
        
        ResponseEntity<TaskDTO> response = taskApiCall.start(startCommand);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldFinishTaskSuccessfully() {
        UserDTO registeredUser = userApiCall.givenRegisteredUser("finishTaskUser@gmail.com", "abc12345", "username");
        TaskDTO startedTask = taskApiCall.givenStartedTask(registeredUser.id());

        FinishTaskCommand finishCommand = new FinishTaskCommand(registeredUser.id(), startedTask.id());
        ResponseEntity<TaskDTO> response = taskApiCall.finish(finishCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldListTasksSuccessfully() {
        UserDTO registeredUser = userApiCall.givenRegisteredUser("userTasks@gmail.com", "abc12345", "username");
        
        taskApiCall.givenCreatedTask(registeredUser.id());
        taskApiCall.givenCreatedTask(registeredUser.id());

        ListTasksQuery query = new ListTasksQuery(registeredUser.id(), null, 0, 10);
        ResponseEntity<TaskDTO[]> response = taskApiCall.list(query);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        TaskDTO[] tasks = response.getBody();
        assertEquals(2, tasks.length);
    }

    @Test
    void shouldRemoveTaskSuccessfully() {
        UserDTO registeredUser = userApiCall.givenRegisteredUser("removeTaskUser@gmail.com", "abc12345", "username");
        
        taskApiCall.givenCreatedTask(registeredUser.id());
        taskApiCall.givenCreatedTask(registeredUser.id());
        TaskDTO taskToRemove = taskApiCall.givenCreatedTask(registeredUser.id());

        ResponseEntity<Void> response = taskApiCall.remove(taskToRemove.id(), registeredUser.id());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ListTasksQuery query = new ListTasksQuery(registeredUser.id(), null, 0, 10);
        ResponseEntity<TaskDTO[]> listResponse = taskApiCall.list(query);

        TaskDTO[] tasks = listResponse.getBody();
        assertEquals(2, tasks.length);

    }
}