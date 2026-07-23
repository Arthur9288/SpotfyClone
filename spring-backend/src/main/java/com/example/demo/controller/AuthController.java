package com.example.demo.controller;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * POST /auth/register
     * Cadastro público — qualquer pessoa pode se registrar.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO dto) {
        AuthResponseDTO response = authService.register(dto);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * POST /auth/login
     * Login público — retorna o token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /auth/google
     * Login com Google — recebe o ID Token do Firebase.
     */
    @PostMapping("/google")
    public ResponseEntity<AuthResponseDTO> loginWithGoogle(@RequestBody @Valid com.example.demo.dto.GoogleLoginRequestDTO dto) {
        AuthResponseDTO response = authService.loginWithGoogle(dto);
        return ResponseEntity.ok(response);
    }
}
