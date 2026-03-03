package com.example.taskprueba.user.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.user.application.command.LoginUserCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.exceptions.IncorrectPassword;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;
import com.example.taskprueba.user.mocks.UserRepositoryMock;

class TestLoginUserService {

	private UserRepositoryMock userRepositoryMock;
    private LoginUserService service;

    @BeforeEach
    void setUp() {
        userRepositoryMock = new UserRepositoryMock();
        service = new LoginUserService(userRepositoryMock);
    }

	@Test
	void shouldLoginSuccessfullyAndReturnUserDTO() {
		User user = UserFactory.create();
		userRepositoryMock.save(user);

		LoginUserCommand command = new LoginUserCommand(user.getAccount().getValue(), "abc12345");

		UserDTO result = service.execute(command);

		assertEquals(user.getId().getValue(), result.id());
		assertEquals(user.getUserName().getValue(), result.name());
		assertEquals(user.getAccount().getValue(), result.account());
		assertFalse(result.isVip());
		assertEquals(0, result.pendingTaskNumber());
		assertEquals(0, result.finishedTaskNumber());
	}

	@Test
	void shouldThrowWhenUserDoesNotExist() {
		LoginUserCommand command = new LoginUserCommand("missing.user@gmail.com", "abc12345");
		assertThrows(UserNotFound.class, () -> service.execute(command));
	}

	@Test
	void shouldThrowWhenPasswordIsIncorrect() {
		User user = UserFactory.create();
		userRepositoryMock.save(user);

		LoginUserCommand command = new LoginUserCommand(user.getAccount().getValue(), "wrong123");

		assertThrows(IncorrectPassword.class, () -> service.execute(command));
	}
}
