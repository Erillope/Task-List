package com.example.taskprueba.task.infrastructure.repository;

import java.util.List;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, String> {
    
    List<TaskEntity> findByUserId(String userId);

    Page<TaskEntity> findByUserId(String userId, Pageable pageable);

    Page<TaskEntity> findByUserIdAndScheduledDate(
        String userId,
        LocalDate scheduledDate,
        Pageable pageable
    );
    
}
