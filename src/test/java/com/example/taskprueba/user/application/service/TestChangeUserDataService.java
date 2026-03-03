package com.example.taskprueba.user.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.user.application.command.ChangeUserDataCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;
import com.example.taskprueba.user.mocks.UserRepositoryMock;

class TestChangeUserDataService {
    UserRepositoryMock repository;
    ChangeUserDataService service;

    @BeforeEach
    void setUp() {
        repository = new UserRepositoryMock();
        service = new ChangeUserDataService(repository, repository);
    }

	@Test
	void shouldChangeUserDataWhenUserExists() {
        User user = UserFactory.create();
		repository.save(user);

		ChangeUserDataCommand command = new ChangeUserDataCommand(
			user.getId().getValue(),
			"new_user",
			"new.user@gmail.com"
		);

		UserDTO userDTO = service.execute(command);

		assertEquals("new_user", userDTO.name());
		assertEquals("new.user@gmail.com", userDTO.account());
	}

	@Test
	void shouldThrowWhenUserDoesNotExist() {
		ChangeUserDataCommand command = new ChangeUserDataCommand(
			"missing-id",
			"new_user",
			"new.user@gmail.com"
		);

		assertThrows(UserNotFound.class, () -> service.execute(command));
	}

}
