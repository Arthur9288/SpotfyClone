package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PlaylistResponseDTO(
    Long id,
    String name,
    String description,
    String coverUrl,
    String ownerName,
    LocalDateTime createdAt,
    List<SongDTO> songs
) {}
