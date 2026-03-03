package com.example.taskprueba.user.factories;

import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.values.Account;
import com.example.taskprueba.user.domain.values.Password;
import com.example.taskprueba.user.domain.values.UserCreationData;
import com.example.taskprueba.user.domain.values.UserName;

public class UserFactory {
    
    public static User create(String username, String account, String password) {
        return User.create(
            new UserCreationData(
                new UserName(username),
                Account.of(account),
                Password.of(password)
            )
        );
    }

    public static User createVip(String username, String account, String password) {
        User user = create(username, account, password);
        user.upgradeToVip();
        return user;
    }

    public static User create() {
        return create("user_test", "user.test@gmail.com", "abc12345");
    }

    public static User createVip() {
        User user = create();
        user.upgradeToVip();
        return user;
    }
}
