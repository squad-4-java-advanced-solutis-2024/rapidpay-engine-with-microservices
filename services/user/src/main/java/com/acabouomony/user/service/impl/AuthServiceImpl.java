package com.acabouomony.user.service.impl;

import com.acabouomony.user.dto.LoginRequest;
import com.acabouomony.user.dto.RegisterRequest;
import com.acabouomony.user.entity.User;
import com.acabouomony.user.repository.UserRepository;
import com.acabouomony.user.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;

@Service("authService")
public class AuthServiceImpl implements AuthService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final UserRepository userRepository;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.cognito.clientId}")
    private String clientId;

    public AuthServiceImpl(
            CognitoIdentityProviderClient cognitoClient,
            UserRepository userRepository
    ) {
        this.cognitoClient = cognitoClient;
        this.userRepository = userRepository;
    }

    @Transactional
    public void register(RegisterRequest request) {
        // Verificar se o usuário já existe
        if (userRepository.findByUsername(request.username()).isPresent())
            throw new RuntimeException("Usuário já existe!");

        // Registrar usuário no AWS Cognito
        AdminCreateUserRequest cognitoRequest = AdminCreateUserRequest.builder()
                .userPoolId(userPoolId)
                .username(request.username())
                .userAttributes(
                        AttributeType.builder().name("email").value(request.email()).build(),
                        AttributeType.builder().name("email_verified").value("true").build()
                )
                .temporaryPassword(request.password())
                .messageAction(MessageActionType.SUPPRESS)
                .build();

        AdminCreateUserResponse cognitoResponse = cognitoClient.adminCreateUser(cognitoRequest);
        String cognitoUserId = cognitoResponse.user().username();

        // Salvar usuário no banco de dados sem a senha
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .cognitoUserId(cognitoUserId)
                .build();
        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        // Autenticar usuário no AWS Cognito
        InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .clientId(clientId)
                .authParameters(Map.of(
                        "USERNAME", request.username(),
                        "PASSWORD", request.password()
                ))
                .build();
        try {
            InitiateAuthResponse authResponse = cognitoClient.initiateAuth(authRequest);
            return authResponse.authenticationResult().idToken();
        } catch (NotAuthorizedException e) {
            throw new RuntimeException("Credenciais inválidas!");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar usuário!");
        }
    }
}
