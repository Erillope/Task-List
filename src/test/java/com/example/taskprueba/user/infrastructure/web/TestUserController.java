package com.example.taskprueba.user.infrastructure.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.taskprueba.user.application.command.ChangeUserDataCommand;
import com.example.taskprueba.user.application.command.ChangeUserPasswordCommand;
import com.example.taskprueba.user.application.command.LoginUserCommand;
import com.example.taskprueba.user.application.command.RemoveVipCommand;
import com.example.taskprueba.user.application.command.UpgradeVipCommand;
import com.example.taskprueba.user.application.dto.UserDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestUserController {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    private UserApiCall userApiCall;

    @BeforeEach
    void setup() {
        userApiCall = new UserApiCall(restTemplate, port, "http://localhost");
    }

    private UserDTO expectedRegisterUserDTO(ChangeUserDataCommand command, UserDTO registeredUser) {
        return UserDTO.builder()
                .id(registeredUser.id())
                .account(command.newAccount() != null ? command.newAccount() : registeredUser.account())
                .name(command.newUserName() != null ? command.newUserName() : registeredUser.name())
                .isVip(registeredUser.isVip())
                .pendingTaskNumber(registeredUser.pendingTaskNumber())
                .finishedTaskNumber(registeredUser.finishedTaskNumber())
                .build();
    }

    @ParameterizedTest
    @CsvSource({
        "usuario@gmail.com,nuevoNombre,nuevo@gmail.com",
        "usuario2@gmail.com,nuevoNombre,",
        "usuario3@gmail.com,,nuevo2@gmail.com"
    })
    void shouldUpdateUserDataSuccessfully(String email, String newName, String newAccount) {
        UserDTO registeredUser = userApiCall.givenRegisteredUser(email, "abc12345", "usuario");

        ChangeUserDataCommand updateCommand = new ChangeUserDataCommand(registeredUser.id(), newName, newAccount);
        ResponseEntity<UserDTO> response = userApiCall.update(updateCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserDTO updatedUserDTO = response.getBody();
        UserDTO expectedUserDTO = expectedRegisterUserDTO(updateCommand, registeredUser);

        assertEquals(updatedUserDTO, expectedUserDTO);
    }

    @ParameterizedTest
    @CsvSource({
        "usuario5@gmail.com,a,nuevo3@gmail.com",
        "usuario6@gmail.com,nuevoNombre,invalid"
    })
    void shouldReturnBadRequestWhenUpdate(String email, String newName, String newAccount) {
        UserDTO registeredUser = userApiCall.givenRegisteredUser(email, "abc12345", "usuario");

        ChangeUserDataCommand updateCommand = new ChangeUserDataCommand(registeredUser.id(), newName, newAccount);
        ResponseEntity<UserDTO> response = userApiCall.update(updateCommand);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void shouldReturnBadRequestWhenUpdateNonExistingUser() {
        ChangeUserDataCommand updateCommand = new ChangeUserDataCommand("non-existing-id", "newName", "newAccount");
        ResponseEntity<UserDTO> response = userApiCall.update(updateCommand);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        UserDTO registeredUser = userApiCall.givenRegisteredUser("user7@gmail.com", "abc12345", "usuario");

        ChangeUserPasswordCommand changePasswordCommand = new ChangeUserPasswordCommand(registeredUser.id(), "newPassword123");
        ResponseEntity<Void> response = userApiCall.changePassword(changePasswordCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        LoginUserCommand loginCommandWithOldPassword = new LoginUserCommand("user7@gmail.com", "abc12345");
        ResponseEntity<UserDTO> loginResponseOldPassword = userApiCall.login(loginCommandWithOldPassword);
        assertEquals(HttpStatus.BAD_REQUEST, loginResponseOldPassword.getStatusCode());

        LoginUserCommand loginCommandWithNewPassword = new LoginUserCommand("user7@gmail.com", "newPassword123");
        ResponseEntity<UserDTO> loginResponseNewPassword = userApiCall.login(loginCommandWithNewPassword);
        assertEquals(HttpStatus.OK, loginResponseNewPassword.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenChangePasswordIsInvalid() {
        UserDTO registeredUser = userApiCall.givenRegisteredUser("user8@gmail.com", "abc12345", "usuario");

        ChangeUserPasswordCommand changePasswordCommand = new ChangeUserPasswordCommand(registeredUser.id(), "invalid");
        ResponseEntity<Void> response = userApiCall.changePassword(changePasswordCommand);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldUpgradeVipSuccessfully() {
        UserDTO registeredUser = userApiCall.givenRegisteredUser("user9@gmail.com", "abc12345", "usuario");

        UpgradeVipCommand upgradeVipCommand = new UpgradeVipCommand(registeredUser.id());
        ResponseEntity<UserDTO> response = userApiCall.upgradeVip(upgradeVipCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserDTO upgradedUser = response.getBody();
        assertTrue(upgradedUser.isVip());
    }

    @Test
    void shouldRemoveVipSuccessfully() {
        UserDTO registeredUser = userApiCall.givenRegisteredUserWithVip("user10@gmail.com", "abc12345", "usuario");

        RemoveVipCommand removeVipCommand = new RemoveVipCommand(registeredUser.id());
        ResponseEntity<UserDTO> response = userApiCall.removeVip(removeVipCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserDTO updatedUser = response.getBody();
        assertFalse(updatedUser.isVip());
    }
}
