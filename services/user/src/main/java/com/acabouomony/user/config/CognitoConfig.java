package com.acabouomony.user.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.regions.Region;

@Configuration("cognitoConfig")
public class CognitoConfig {

    @Value("${aws.region}")
    private String REGION;

    @Bean("cognitoClient")
    public CognitoIdentityProviderClient cognitoClient() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.of(REGION))
                .build();
    }
}