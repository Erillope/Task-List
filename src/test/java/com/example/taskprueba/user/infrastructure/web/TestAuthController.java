package com.example.taskprueba.user.infrastructure.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.taskprueba.user.application.command.LoginUserCommand;
import com.example.taskprueba.user.application.command.RegisterUserCommand;
import com.example.taskprueba.user.application.dto.UserDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestAuthController {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private UserApiCall userApiCall;

    @BeforeEach
    void setup() {
        userApiCall = new UserApiCall(restTemplate, port, "http://localhost");
    }

    private UserDTO expectedRegisterUserDTO(RegisterUserCommand command, String userId) {
        return UserDTO.builder()
                .id(userId)
                .account(command.account())
                .name(command.userName())
                .isVip(false)
                .pendingTaskNumber(0)
                .finishedTaskNumber(0)
                .build();
    }

    @ParameterizedTest
    @CsvSource({
        "usuario@gmail.com,abc12345,usuario",
        "+593961530738,abc12345,usuario",
    })
    void shouldRegisterUserSuccessfully(String account, String password, String username) {
        RegisterUserCommand registerCommand = new RegisterUserCommand(account, password, username);
        ResponseEntity<UserDTO> response = userApiCall.register(registerCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserDTO userDTO = response.getBody();
        UserDTO expectedUserDTO = expectedRegisterUserDTO(registerCommand, userDTO.id());

        assertEquals(userDTO, expectedUserDTO);
    }

    @ParameterizedTest
    @CsvSource({
        " , , ",
        "usuario,abc12345,usuario",
        "invalidPasswordUser@gmail.com,wrongpassword,usuario",
        "invalidUsernameUser@gmail.com,abc12345,a"
    })
    void shouldReturnBadRequestWhenRegister(String account, String password, String username) {
        RegisterUserCommand registerCommand = new RegisterUserCommand(account, password, username);
        ResponseEntity<UserDTO> response = userApiCall.register(registerCommand);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldLoginUserSuccessfully() {
        userApiCall.givenRegisteredUser("login@gmail.com", "abc12345", "usuario");
        
        LoginUserCommand loginCommand = new LoginUserCommand("login@gmail.com", "abc12345");
        ResponseEntity<UserDTO> response = userApiCall.login(loginCommand);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @ParameterizedTest
    @CsvSource({
        " , ",
        "loginIncorrectPassword@gmail.com, wrongpassword",
    })
    void shouldReturnBadRequestWhenLogin() {
        LoginUserCommand loginCommand = new LoginUserCommand("", "");
        ResponseEntity<UserDTO> response = userApiCall.login(loginCommand);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenLoginWithIncorrectPassword() {
        userApiCall.givenRegisteredUser("loginIncorrectPassword@gmail.com", "abc12345", "usuario");

        LoginUserCommand loginCommand = new LoginUserCommand("loginIncorrectPassword@gmail.com", "wrongpassword");
        ResponseEntity<UserDTO> response = userApiCall.login(loginCommand);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
