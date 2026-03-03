package com.example.taskprueba.user.infrastructure.web;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import com.example.taskprueba.user.application.command.ChangeUserDataCommand;
import com.example.taskprueba.user.application.command.ChangeUserPasswordCommand;
import com.example.taskprueba.user.application.command.LoginUserCommand;
import com.example.taskprueba.user.application.command.RegisterUserCommand;
import com.example.taskprueba.user.application.command.RemoveVipCommand;
import com.example.taskprueba.user.application.command.UpgradeVipCommand;
import com.example.taskprueba.user.application.dto.UserDTO;

public class UserApiCall {
    
    private final TestRestTemplate restTemplate;
    private final int port;
    private final String hostURL;

    public UserApiCall(TestRestTemplate restTemplate, int port, String hostURL) {
        this.restTemplate = restTemplate;
        this.port = port;
        this.hostURL = hostURL;
        ensurePatchSupport();
    }

    private void ensurePatchSupport() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    public ResponseEntity<UserDTO> register(RegisterUserCommand registerCommand) {
        return restTemplate.postForEntity(hostURL + ":" + port + "/auth/register", registerCommand, UserDTO.class);
    }

    public ResponseEntity<UserDTO> login(LoginUserCommand loginCommand) {
        return restTemplate.postForEntity(hostURL + ":" + port + "/auth/login", loginCommand, UserDTO.class);
    }

    public ResponseEntity<UserDTO> update(ChangeUserDataCommand updateCommand) {
        return restTemplate.exchange("http://localhost:" + port + "/users", HttpMethod.PUT, new HttpEntity<>(updateCommand), UserDTO.class);
    }

    public ResponseEntity<Void> changePassword(ChangeUserPasswordCommand changePasswordCommand) {
        return restTemplate.exchange(
            "http://localhost:" + port + "/users/password",
            HttpMethod.PATCH,
            new HttpEntity<>(changePasswordCommand),
            Void.class
        );
    }

    public ResponseEntity<UserDTO> upgradeVip(UpgradeVipCommand upgradeVipCommand) {
        return restTemplate.exchange(
            "http://localhost:" + port + "/users/upgrade-vip",
            HttpMethod.PATCH,
            new HttpEntity<>(upgradeVipCommand),
            UserDTO.class
        );
    }

    public ResponseEntity<UserDTO> removeVip(RemoveVipCommand removeVipCommand) {
        return restTemplate.exchange(
            "http://localhost:" + port + "/users/remove-vip",
            HttpMethod.PATCH,
            new HttpEntity<>(removeVipCommand),
            UserDTO.class
        );
    } 

    public UserDTO givenRegisteredUser(String account, String password, String username) {
        RegisterUserCommand registerCommand = new RegisterUserCommand(account, password, username);
        ResponseEntity<UserDTO> response = register(registerCommand);
        return response.getBody();
    }

    public UserDTO givenRegisteredUserWithVip(String account, String password, String username) {
        UserDTO registeredUser = givenRegisteredUser(account, password, username);

        UpgradeVipCommand upgradeVipCommand = new UpgradeVipCommand(registeredUser.id());
        ResponseEntity<UserDTO> response = upgradeVip(upgradeVipCommand);
        
        return response.getBody();
    }

}
