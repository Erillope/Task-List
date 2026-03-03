package com.example.taskprueba.task.mocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.task.domain.persistence_ports.DeleteTask;
import com.example.taskprueba.task.domain.persistence_ports.GetTask;
import com.example.taskprueba.task.domain.persistence_ports.SaveTask;
import com.example.taskprueba.task.domain.values.TaskFilterQuery;

public class TaskRepositoryMock implements GetTask, SaveTask, DeleteTask {
    
    private final Map<String, Task> tasksById = new HashMap<>();

    @Override
    public boolean existsById(String id) {
        return tasksById.containsKey(id);
    }

    @Override
    public Optional<Task> getTaskById(String id) {
        return Optional.ofNullable(tasksById.get(id));
    }

    @Override
    public List<Task> filterTasks(TaskFilterQuery query) {
        List<Task> tasks = tasksById.values().stream()
            .filter(task -> query.scheduledDate() == null || task.getSchedule().equals(query.scheduledDate()))
            .toList();

        if (query.page() == null || query.size() == null) {
            return tasks;
        }

        int page = query.page().getValue();
        int size = query.size().getValue();

        if (size <= 0) {
            return List.of();
        }

        int fromIndex = page * size;
        if (fromIndex >= tasks.size()) {
            return List.of();
        }

        int toIndex = Math.min(fromIndex + size, tasks.size());
        return tasks.subList(fromIndex, toIndex);
    }

    @Override
    public List<Task> getTasksByUserId(String userId) {
        return new ArrayList<>(tasksById.values());
    }

    @Override
    public void save(Task task) {
        tasksById.put(task.getId().getValue(), task);
    }

    @Override
    public void delete(String id) {
        tasksById.remove(id);
    }

    public void clear() {
        tasksById.clear();
    }

    public int size() {
        return tasksById.size();
    }
}
