package com.example.demo.dto;

public record AuthResponseDTO(
    String token,
    String name,
    String email
) {}
