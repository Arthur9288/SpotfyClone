package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record PlaylistRequestDTO(
    @NotBlank(message = "O nome da playlist é obrigatório")
    String name,

    String description,
    String coverUrl
) {}
