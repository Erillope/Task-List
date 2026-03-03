package com.example.taskprueba.user.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskprueba.user.application.command.UpgradeVipCommand;
import com.example.taskprueba.user.application.dto.UserDTO;
import com.example.taskprueba.user.application.exceptions.UserNotFound;
import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.factories.UserFactory;
import com.example.taskprueba.user.mocks.UserRepositoryMock;

class TestUpgradeVipService {

    private UserRepositoryMock repository;
    private UpgradeVipService service;

    @BeforeEach
    void setUp() {
        repository = new UserRepositoryMock();
        service = new UpgradeVipService(repository, repository);
    }

	@Test
	void shouldUpgradeUserToVipWhenUserExists() {
		User user = UserFactory.create();
		repository.save(user);

		UpgradeVipCommand command = new UpgradeVipCommand(user.getId().getValue());
		UserDTO userDTO = service.execute(command);

		assertTrue(userDTO.isVip());
	}

	@Test
	void shouldThrowWhenUserDoesNotExist() {
		UpgradeVipCommand command = new UpgradeVipCommand("missing-id");
		assertThrows(UserNotFound.class, () -> service.execute(command));
	}
}
