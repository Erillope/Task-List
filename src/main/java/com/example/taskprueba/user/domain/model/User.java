package com.example.taskprueba.user.domain.model;

import com.example.taskprueba.common.values.NonNegativeNumber;
import com.example.taskprueba.task.domain.model.Task;
import com.example.taskprueba.user.domain.values.Account;
import com.example.taskprueba.user.domain.values.Password;
import com.example.taskprueba.user.domain.values.UserCreationData;
import com.example.taskprueba.user.domain.values.UserID;
import com.example.taskprueba.user.domain.values.UserName;
import com.example.taskprueba.user.domain.values.UserSnapshot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class User {
    
    private final UserID id;
    private UserName userName;
    private Account account;
    private Password password;
    private boolean isVip;
    private NonNegativeNumber pendingTaskNumber;
    private NonNegativeNumber finishedTaskNumber;

    public boolean isMe(String account, String password) {
        return this.account.getValue().equals(account) && this.password.matches(password);
    }

    public void changePassword(Password newPassword) {
        this.password = newPassword;
    }

    public void changeAccount(Account newAccount) {
        this.account = newAccount;
    }

    public void changeUserName(UserName newUserName) {
        this.userName = newUserName;
    }

    public void addPendingTask(Task task) {
        this.pendingTaskNumber = new NonNegativeNumber(this.pendingTaskNumber.getValue() + 1);
        task.setUserId(this.id);
    }

    public void finishTask() {
        this.pendingTaskNumber = new NonNegativeNumber(this.pendingTaskNumber.getValue() - 1);
        this.finishedTaskNumber = new NonNegativeNumber(this.finishedTaskNumber.getValue() + 1);
    }

    public void removePendingTask(Task task) {
        this.pendingTaskNumber = new NonNegativeNumber(this.pendingTaskNumber.getValue() - 1);
        task.setUserId(null);
    }

    public void upgradeToVip() {
        this.isVip = true;
    }

    public void removeVip() {
        this.isVip = false;
    }

    public static User create(UserCreationData data) {
        return User.builder()
            .id(UserID.uuid())
            .userName(data.userName())
            .account(data.account())
            .password(data.password())
            .isVip(false)
            .pendingTaskNumber(NonNegativeNumber.zero())
            .finishedTaskNumber(NonNegativeNumber.zero())
            .build();
    }

    public static User restore(UserSnapshot snapshot) {
        return User.builder()
            .id(snapshot.id())
            .userName(snapshot.userName())
            .account(snapshot.account())
            .password(snapshot.password())
            .isVip(snapshot.isVip())
            .pendingTaskNumber(snapshot.pendingTaskNumber())
            .finishedTaskNumber(snapshot.finishedTaskNumber())
            .build();
    }

}
