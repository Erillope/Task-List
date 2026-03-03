package com.example.taskprueba.user.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.values.Password;
import com.example.taskprueba.user.factories.UserFactory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Import(UserRepository.class)
class TestUserRepository {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserJpaRepository userJpaRepository;

	private UserEntity createUserEntity() {
		User user = UserFactory.create();
        return UserEntityMapper.toEntity(user);
	}

	@Test
	void shouldDelegateExistsById() {
		UserEntity entity = createUserEntity();
		userJpaRepository.save(entity);

		boolean exists = userRepository.existsById(entity.getId());

		assertTrue(exists);
	}

	@Test
	void shouldDelegateExistsByAccount() {
		UserEntity entity = createUserEntity();
		userJpaRepository.save(entity);

		boolean exists = userRepository.existsByAccount(entity.getAccount());

		assertTrue(exists);
	}

	@Test
	void shouldReturnMappedDomainUserWhenGetUserByIdExists() {
        User user = UserFactory.create();
        user.changePassword(Password.of("abc12345"));
		UserEntity entity = createUserEntity();

		userJpaRepository.save(entity);

		Optional<User> result = userRepository.getUserById(entity.getId());

		assertTrue(result.isPresent());
		assertEquals(entity.getId(), result.orElseThrow().getId().getValue());
		assertEquals(entity.getUserName(), result.orElseThrow().getUserName().getValue());
		assertEquals(entity.getAccount(), result.orElseThrow().getAccount().getValue());
		assertTrue(result.orElseThrow().isMe(entity.getAccount(), "abc12345"));
	}

	@Test
	void shouldReturnMappedDomainUserWhenGetUserByAccountExists() {
		UserEntity entity = createUserEntity();

		userJpaRepository.save(entity);

		Optional<User> result = userRepository.getUserByAccount(entity.getAccount());

		assertTrue(result.isPresent());
	}

	@Test
	void shouldReturnEmptyOptionalWhenUserNotFound() {
		Optional<User> byId = userRepository.getUserById("missing-id");
		Optional<User> byAccount = userRepository.getUserByAccount("missing@gmail.com");

		assertFalse(byId.isPresent());
		assertFalse(byAccount.isPresent());
	}

	@Test
	void shouldMapAndSaveUserEntity() {
		User user = UserFactory.create();

		userRepository.save(user);

		UserEntity savedEntity = userJpaRepository.findById(user.getId().getValue()).orElseThrow();
		assertEquals(user.getId().getValue(), savedEntity.getId());
		assertEquals(user.getUserName().getValue(), savedEntity.getUserName());
		assertEquals(user.getAccount().getValue(), savedEntity.getAccount());
		assertFalse(savedEntity.getPassword().isEmpty());
		assertEquals(user.isVip(), savedEntity.isVip());
		assertEquals(user.getPendingTaskNumber().getValue(), savedEntity.getPendingTaskNumber());
		assertEquals(user.getFinishedTaskNumber().getValue(), savedEntity.getFinishedTaskNumber());
	}
}
