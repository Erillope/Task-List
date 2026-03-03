package com.example.taskprueba.user.application.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.user.application.command.RemoveVipCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;
import com.example.taskprueba.user.mocks.UserRepositoryMock;

class TestRemoveVipService {

    private UserRepositoryMock repository;
    private RemoveVipService service;

    @BeforeEach
    void setUp() {
        repository = new UserRepositoryMock();
        service = new RemoveVipService(repository, repository);
    }

	@Test
	void shouldRemoveVipWhenUserExists() {
		User user = UserFactory.createVip();
		repository.save(user);

		RemoveVipCommand command = new RemoveVipCommand(user.getId().getValue());

		UserDTO userDTO = service.execute(command);

		assertFalse(userDTO.isVip());
	}

	@Test
	void shouldThrowWhenUserDoesNotExist() {
		RemoveVipCommand command = new RemoveVipCommand("missing-id");

		assertThrows(UserNotFound.class, () -> service.execute(command));
	}
}
