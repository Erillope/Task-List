package com.example.taskprueba.user.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.user.application.command.ChangeUserPasswordCommand;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;
import com.example.taskprueba.user.mocks.UserRepositoryMock;

class TestChangeUserPasswordService {

    UserRepositoryMock repository;
    ChangeUserPasswordService service;

    @BeforeEach
    void setUp() {
        repository = new UserRepositoryMock();
        service = new ChangeUserPasswordService(repository, repository);
    }

	@Test
	void shouldChangePasswordWhenUserExists() {
        User user = UserFactory.create();
		repository.save(user);

		ChangeUserPasswordCommand command = new ChangeUserPasswordCommand(user.getId().getValue(), "new12345");

		service.execute(command);

		User updatedUser = repository.getUserById(user.getId().getValue()).orElseThrow();
		assertTrue(updatedUser.isMe(user.getAccount().getValue(), "new12345"));
	}

	@Test
	void shouldThrowWhenUserDoesNotExist() {
		ChangeUserPasswordCommand command = new ChangeUserPasswordCommand("missing-id", "new12345");

		assertThrows(UserNotFound.class, () -> service.execute(command));
	}

}
