package com.example.taskprueba.task.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.persistence_ports.DeleteTask;
import com.example.taskprueba.task.domain.persistence_ports.GetTask;
import com.example.taskprueba.task.domain.persistence_ports.SaveTask;
import com.example.taskprueba.task.domain.values.TaskFilterQuery;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskRepository implements GetTask, SaveTask, DeleteTask {

    private final TaskJpaRepository taskJpaRepository;

    @Override
    public boolean existsById(String id) {
        return taskJpaRepository.existsById(id);
    }

    @Override
    public Optional<Task> getTaskById(String id) {
        Optional<TaskEntity> entity = taskJpaRepository.findById(id);
        return entity.map(TaskEntityMapper::toDomain);
    }

    private Page<TaskEntity> getTasksByQuery(TaskFilterQuery query) {
        int page = query.page().getValue();
        int size = query.size().getValue();
        return taskJpaRepository.findByUserIdAndScheduledDateAndStartTimeAndEndTime(
                query.userId().getValue(),
                query.scheduledDate().getScheduledDate(),
                query.scheduledDate().getStartTime(),
                query.scheduledDate().getEndTime(),
                PageRequest.of(page, size));
    }

    @Override
    public List<Task> filterTasks(TaskFilterQuery query) {
        Page<TaskEntity> entities;
        if (query.scheduledDate() != null) {
            entities = getTasksByQuery(query);
        } else {
            entities = taskJpaRepository.findByUserId(
                    query.userId().getValue(),
                    PageRequest.of(query.page().getValue(), query.size().getValue()));
        }

        return entities.stream().map(TaskEntityMapper::toDomain).toList();
    }

    @Override
    public List<Task> getTasksByUserId(String userId) {
        List<TaskEntity> entities = taskJpaRepository.findByUserId(userId);
        return entities.stream().map(TaskEntityMapper::toDomain).toList();
    }

    @Override
    public void save(Task task) {
        TaskEntity entity = TaskEntityMapper.toEntity(task);
        taskJpaRepository.save(entity);
    }

    @Override
    public void delete(String id) {
        taskJpaRepository.deleteById(id);
    }

}