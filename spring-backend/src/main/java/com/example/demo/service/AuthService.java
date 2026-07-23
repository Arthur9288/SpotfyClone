package com.example.demo.service;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Já existe um usuário com este email.");
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
        String token = tokenService.generateToken(user);
        return new AuthResponseDTO(token, user.getName(), user.getEmail());
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas."));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        String token = tokenService.generateToken(user);
        return new AuthResponseDTO(token, user.getName(), user.getEmail());
    }

    public AuthResponseDTO loginWithGoogle(com.example.demo.dto.GoogleLoginRequestDTO dto) {
        // Firebase ID tokens devem ser validados via Firebase Identity Toolkit,
        // não pelo endpoint tokeninfo do Google OAuth2.
        String firebaseApiKey = "AIzaSyDzlIafnNaU-qLJGh1UTnAgpGSOzIbWFak";
        String verifyUrl = "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=" + firebaseApiKey;

        try {
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();

            // Monta o corpo da requisição para o Firebase
            java.util.Map<String, String> requestBody = java.util.Map.of("idToken", dto.token());

            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> firebaseResponse = restTemplate.postForObject(
                    verifyUrl,
                    requestBody,
                    java.util.Map.class
            );

            if (firebaseResponse == null || !firebaseResponse.containsKey("users")) {
                throw new BadCredentialsException("Token Firebase inválido.");
            }

            @SuppressWarnings("unchecked")
            java.util.List<java.util.Map<String, Object>> users =
                    (java.util.List<java.util.Map<String, Object>>) firebaseResponse.get("users");

            if (users == null || users.isEmpty()) {
                throw new BadCredentialsException("Usuário não encontrado no Firebase.");
            }

            java.util.Map<String, Object> firebaseUser = users.get(0);
            String email       = (String) firebaseUser.get("email");
            String displayName = (String) firebaseUser.get("displayName");

            if (email == null) {
                throw new BadCredentialsException("Email não encontrado no token Firebase.");
            }

            // Busca o usuário ou cria um novo automaticamente
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = User.builder()
                        .name(displayName != null ? displayName : "Usuário Google")
                        .email(email)
                        .password(passwordEncoder.encode(java.util.UUID.randomUUID().toString()))
                        .role(UserRole.USER)
                        .build();
                return userRepository.save(newUser);
            });

            String appToken = tokenService.generateToken(user);
            return new AuthResponseDTO(appToken, user.getName(), user.getEmail());

        } catch (BadCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new BadCredentialsException("Falha ao autenticar com o Google: " + e.getMessage());
        }
    }
}
