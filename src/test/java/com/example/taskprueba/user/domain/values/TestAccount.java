package com.example.taskprueba.user.domain.values;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import com.example.taskprueba.user.domain.exceptions.InvalidAccount;

class TestAccount {

	@Test
	void shouldCreateGmailAccount() {
		String gmail = "user.test@gmail.com";

		Account account = Account.of(gmail);

		assertInstanceOf(GmailAccount.class, account);
		assertEquals(gmail, account.getValue());
	}

	@Test
	void shouldCreatePhoneNumberAccount() {
		String phoneNumber = "+573001112233";

		Account account = Account.of(phoneNumber);

		assertInstanceOf(PhoneNumberAccount.class, account);
		assertEquals(phoneNumber, account.getValue());
	}

    @Test
    void shouldThrowInvalidAccountException() {
        String invalidAccount = "invalid_account";

        try {
            Account.of(invalidAccount);
        } catch (Exception e) {
            assertInstanceOf(InvalidAccount.class, e);
        }
    }
}
