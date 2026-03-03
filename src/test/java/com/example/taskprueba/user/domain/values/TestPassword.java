package com.example.taskprueba.user.domain.values;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import com.example.taskprueba.user.domain.exceptions.InvalidPassword;

class TestPassword {

	@Test
	void shouldHashAndMatchPasswordWhenUsingOf() {
		String rawPassword = "abc12345";

		Password password = Password.of(rawPassword);

		assertTrue(password.matches(rawPassword));
		assertFalse(password.matches("abc12346"));
	}

	@Test
	void shouldCreateFromHashedPassword() {
		String rawPassword = "abc12345";
		String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

		Password password = Password.hashed(hashedPassword);

		assertTrue(password.matches(rawPassword));
	}

	@Test
	void shouldThrowWhenPasswordIsInvalid() {
		assertThrows(InvalidPassword.class, () -> Password.of("abcdefg"));
	}

	@Test
	void shouldThrowWhenPasswordIsNull() {
		assertThrows(InvalidPassword.class, () -> Password.of(null));
	}
}
