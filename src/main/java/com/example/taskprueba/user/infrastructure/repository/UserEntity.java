package com.example.taskprueba.user.infrastructure.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    
    @Id
    private String id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String account;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isVip;

    @Column(nullable = false, name = "pending_task_number")
    private int pendingTaskNumber;

    @Column(nullable = false, name = "finished_task_number")
    private int finishedTaskNumber;
    
}
