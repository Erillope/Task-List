package com.example.taskprueba.user.mocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.taskprueba.user.domain.model.User;
import com.example.taskprueba.user.domain.persistence_ports.GetUser;
import com.example.taskprueba.user.domain.persistence_ports.SaveUser;

public class UserRepositoryMock implements GetUser, SaveUser {

	private final Map<String, User> usersById = new HashMap<>();

	@Override
	public boolean existsById(String id) {
		return usersById.containsKey(id);
	}

	@Override
	public boolean existsByAccount(String account) {
		return usersById.values().stream()
			.anyMatch(user -> user.getAccount().getValue().equals(account));
	}

	@Override
	public Optional<User> getUserById(String id) {
		return Optional.ofNullable(usersById.get(id));
	}

	@Override
	public Optional<User> getUserByAccount(String account) {
		return usersById.values().stream()
			.filter(user -> user.getAccount().getValue().equals(account))
			.findFirst();
	}

	@Override
	public void save(User user) {
		usersById.put(user.getId().getValue(), user);
	}
}
