package com.example.demo.service;

import com.example.demo.dto.ArtistDTO;
import com.example.demo.dto.SongDTO;
import com.example.demo.model.Artist;
import com.example.demo.model.Song;
import com.example.demo.repository.ArtistRepository;
import com.example.demo.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.cache.annotation.Cacheable;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    /**
     * Lista todos os artistas — sem músicas para economizar payload.
     */
    @Cacheable("artists")
    public List<ArtistDTO> findAll() {
        return artistRepository.findAll()
                .stream()
                .map(this::toDTOWithoutSongs)
                .toList();
    }

    /**
     * Busca artista por ID — inclui a lista de músicas para a tela de detalhe.
     */
    public ArtistDTO findById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Artista não encontrado: id=" + id));

        List<SongDTO> songs = songRepository.findByArtistId(id)
                .stream()
                .map(this::songToDTO)
                .toList();

        return new ArtistDTO(
                artist.getId(),
                artist.getName(),
                artist.getImageUrl(),
                artist.getBannerUrl(),
                songs
        );
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /** DTO sem songs — usado na listagem para evitar N+1 queries. */
    private ArtistDTO toDTOWithoutSongs(Artist artist) {
        return new ArtistDTO(
                artist.getId(),
                artist.getName(),
                artist.getImageUrl(),
                artist.getBannerUrl(),
                null
        );
    }

    private SongDTO songToDTO(Song song) {
        return new SongDTO(
                song.getId(),
                song.getName(),
                song.getDuration(),
                song.getImageUrl(),
                song.getAudioUrl(),
                song.getArtist().getName(),
                song.getArtist().getId()
        );
    }
}
