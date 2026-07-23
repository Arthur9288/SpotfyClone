package com.example.demo.dto;

public record SongDTO(
    Long id,
    String name,
    String duration,
    String imageUrl,
    String audioUrl,
    String artistName,
    Long artistId
) implements java.io.Serializable {}
