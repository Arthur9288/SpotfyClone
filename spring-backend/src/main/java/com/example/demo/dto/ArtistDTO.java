package com.example.demo.dto;

import java.util.List;

/**
 * DTO principal de artista — retornado tanto na listagem quanto no detalhe.
 * O campo songs só é preenchido na resposta de GET /api/artists/{id}.
 * Na listagem (GET /api/artists) songs virá como null para economizar payload.
 */
public record ArtistDTO(
    Long id,
    String name,
    String imageUrl,
    String bannerUrl,
    List<SongDTO> songs
) implements java.io.Serializable {}
