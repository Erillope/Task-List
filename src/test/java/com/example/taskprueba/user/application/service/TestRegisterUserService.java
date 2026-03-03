package com.example.taskprueba.user.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.user.application.command.RegisterUserCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.exceptions.UserAlreadyRegistered;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;
import com.example.taskprueba.user.mocks.UserRepositoryMock;

class TestRegisterUserService {
    UserRepositoryMock userRepositoryMock;
    RegisterUserService service;

    @BeforeEach
    void setup() {
        userRepositoryMock = new UserRepositoryMock();
        service = new RegisterUserService(userRepositoryMock, userRepositoryMock);
    }

	@Test
	void shouldRegisterUserWhenAccountIsNotRegistered() {
		RegisterUserCommand command = new RegisterUserCommand(
			"new.user@gmail.com",
			"abc12345",
			"new_user"
		);

		UserDTO userDTO = service.execute(command);

		assertTrue(userRepositoryMock.existsByAccount(command.account()));
		assertEquals("new_user", userDTO.name());
		assertTrue(userDTO.account().equals(command.account()) && userDTO.isVip() == false);
	}

	@Test
	void shouldThrowWhenAccountIsAlreadyRegistered() {
		User existingUser = UserFactory.create();
		userRepositoryMock.save(existingUser);

		RegisterUserCommand command = new RegisterUserCommand(
			existingUser.getAccount().getValue(),
			"abc12345",
			"another_user"
		);

		assertThrows(UserAlreadyRegistered.class, () -> service.execute(command));
	}
}
