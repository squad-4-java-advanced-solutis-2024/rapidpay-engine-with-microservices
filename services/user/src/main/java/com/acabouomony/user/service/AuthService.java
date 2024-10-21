package com.acabouomony.user.service;

import com.acabouomony.user.dto.LoginRequest;
import com.acabouomony.user.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Service responsible for user authentication")
public interface AuthService {
    void register(RegisterRequest request);
    String login(LoginRequest request);
}
