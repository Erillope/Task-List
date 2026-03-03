package com.example.taskprueba.task.infrastructure.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskprueba.task.application.command.AddTaskCommand;
import com.example.taskprueba.task.application.command.ChangeTaskDataCommand;
import com.example.taskprueba.task.application.command.FinishTaskCommand;
import com.example.taskprueba.task.application.command.RemoveTaskCommand;
import com.example.taskprueba.task.application.command.StartTaskCommand;
import com.example.taskprueba.task.application.dto.TaskDTO;
import com.example.taskprueba.task.application.query.ListTasksQuery;
import com.example.taskprueba.task.application.usecase.AddTaskUseCase;
import com.example.taskprueba.task.application.usecase.ChangeTaskDataUseCase;
import com.example.taskprueba.task.application.usecase.FinishTaskUseCase;
import com.example.taskprueba.task.application.usecase.ListTaskUseCase;
import com.example.taskprueba.task.application.usecase.RemoveTaskUseCase;
import com.example.taskprueba.task.application.usecase.StartTaskUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    
    private final AddTaskUseCase addTaskUseCase;
    private final ChangeTaskDataUseCase changeTaskDataUseCase;
    private final StartTaskUseCase startTaskUseCase;
    private final FinishTaskUseCase finishTaskUseCase;
    private final RemoveTaskUseCase removeTaskUseCase;
    private final ListTaskUseCase listTaskUseCase;

    @PostMapping
    public ResponseEntity<TaskDTO> add(@Valid @RequestBody AddTaskCommand command) {
        TaskDTO task = addTaskUseCase.execute(command);
        return ResponseEntity.ok(task);
    }

    @PutMapping
    public ResponseEntity<TaskDTO> update(@Valid @RequestBody ChangeTaskDataCommand command) {
        TaskDTO task = changeTaskDataUseCase.execute(command);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/start")
    public ResponseEntity<TaskDTO> start(@Valid @RequestBody StartTaskCommand command) {
        TaskDTO task = startTaskUseCase.execute(command);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/finish")
    public ResponseEntity<TaskDTO> finish(@Valid @RequestBody FinishTaskCommand command) {
        TaskDTO task = finishTaskUseCase.execute(command);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping
    public void remove(@RequestParam String taskID, @RequestParam String userID) {
        RemoveTaskCommand command = new RemoveTaskCommand(userID, taskID);
        removeTaskUseCase.execute(command);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> list(
        @RequestParam String userID,
        @RequestParam(required = false) LocalDate scheduledDate,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        ListTasksQuery query = new ListTasksQuery(userID, scheduledDate, page, size);
        List<TaskDTO> tasks = listTaskUseCase.execute(query);
        return ResponseEntity.ok(tasks);
    }

}
