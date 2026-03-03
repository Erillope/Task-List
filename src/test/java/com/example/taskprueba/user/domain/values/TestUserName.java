package com.example.taskprueba.user.domain.values;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.example.taskprueba.user.domain.exceptions.InvalidUserName;

class TestUserName {

	@Test
	void shouldCreateUserNameWhenValueIsValid() {
		String value = "user_123";

		UserName userName = new UserName(value);

		assertEquals(value, userName.getValue());
	}

	@Test
	void shouldThrowWhenUserNameHasInvalidFormat() {
		assertThrows(InvalidUserName.class, () -> new UserName("ab"));
	}

	@Test
	void shouldThrowWhenUserNameIsEmpty() {
		assertThrows(InvalidUserName.class, () -> new UserName(""));
	}

	@Test
	void shouldThrowWhenUserNameIsNull() {
		assertThrows(InvalidUserName.class, () -> new UserName(null));
	}
}
