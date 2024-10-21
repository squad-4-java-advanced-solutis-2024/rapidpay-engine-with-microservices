package com.acabouomony.user.controller;

import com.acabouomony.user.dto.LoginRequest;
import com.acabouomony.user.dto.RegisterRequest;
import com.acabouomony.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de Autenticação.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para registro de usuários.
     *
     * @param request dados de registro.
     * @return mensagem de sucesso.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }

    /**
     * Endpoint para login de usuários.
     *
     * @param request dados de login.
     * @return token de autenticação.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }
}
