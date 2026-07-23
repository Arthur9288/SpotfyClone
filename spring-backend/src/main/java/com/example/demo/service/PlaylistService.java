package com.example.demo.service;

import com.example.demo.dto.PlaylistRequestDTO;
import com.example.demo.dto.PlaylistResponseDTO;
import com.example.demo.dto.SongDTO;
import com.example.demo.model.Playlist;
import com.example.demo.model.Song;
import com.example.demo.model.User;
import com.example.demo.repository.PlaylistRepository;
import com.example.demo.repository.SongRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    // ── Obter o usuário autenticado da sessão atual ──────────────────────────────
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }

    // ── Criar playlist ───────────────────────────────────────────────────────────
    @Transactional
    public PlaylistResponseDTO create(PlaylistRequestDTO dto) {
        User owner = getAuthenticatedUser();
        Playlist playlist = Playlist.builder()
                .name(dto.name())
                .description(dto.description())
                .coverUrl(dto.coverUrl())
                .owner(owner)
                .build();
        return toDTO(playlistRepository.save(playlist));
    }

    // ── Listar playlists do usuário ──────────────────────────────────────────────
    public List<PlaylistResponseDTO> findMyPlaylists() {
        User owner = getAuthenticatedUser();
        return playlistRepository.findByOwnerId(owner.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ── Listar todas as playlists públicas (Home) ────────────────────────────────
    public List<PlaylistResponseDTO> findAllPublicPlaylists() {
        return playlistRepository.findAll()
                .stream()
                .filter(p -> p.getOwner().getEmail().equals("spotify@system.com"))
                .map(this::toDTO)
                .toList();
    }

    // ── Buscar playlist específica publicamente ──────────────────────────────────
    public PlaylistResponseDTO findByIdPublic(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Playlist não encontrada: id=" + id));
        return toDTO(playlist);
    }

    // ── Buscar playlist por ID (somente do próprio usuário) ──────────────────────
    public PlaylistResponseDTO findById(Long id) {
        User owner = getAuthenticatedUser();
        Playlist playlist = playlistRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new NoSuchElementException("Playlist não encontrada ou não autorizada."));
        return toDTO(playlist);
    }

    // ── Atualizar nome/descrição da playlist ─────────────────────────────────────
    @Transactional
    public PlaylistResponseDTO update(Long id, PlaylistRequestDTO dto) {
        User owner = getAuthenticatedUser();
        Playlist playlist = playlistRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new NoSuchElementException("Playlist não encontrada ou não autorizada."));

        playlist.setName(dto.name());
        if (dto.description() != null) playlist.setDescription(dto.description());
        if (dto.coverUrl() != null) playlist.setCoverUrl(dto.coverUrl());

        return toDTO(playlistRepository.save(playlist));
    }

    // ── Deletar playlist ─────────────────────────────────────────────────────────
    @Transactional
    public void delete(Long id) {
        User owner = getAuthenticatedUser();
        Playlist playlist = playlistRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new NoSuchElementException("Playlist não encontrada ou não autorizada."));
        playlistRepository.delete(playlist);
    }

    // ── Adicionar música à playlist ──────────────────────────────────────────────
    @Transactional
    public PlaylistResponseDTO addSong(Long playlistId, Long songId) {
        User owner = getAuthenticatedUser();
        Playlist playlist = playlistRepository.findByIdAndOwnerId(playlistId, owner.getId())
                .orElseThrow(() -> new NoSuchElementException("Playlist não encontrada ou não autorizada."));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new NoSuchElementException("Música não encontrada: id=" + songId));

        if (!playlist.getSongs().contains(song)) {
            playlist.getSongs().add(song);
        }

        return toDTO(playlistRepository.save(playlist));
    }

    // ── Remover música da playlist ───────────────────────────────────────────────
    @Transactional
    public PlaylistResponseDTO removeSong(Long playlistId, Long songId) {
        User owner = getAuthenticatedUser();
        Playlist playlist = playlistRepository.findByIdAndOwnerId(playlistId, owner.getId())
                .orElseThrow(() -> new NoSuchElementException("Playlist não encontrada ou não autorizada."));

        playlist.getSongs().removeIf(s -> s.getId().equals(songId));
        return toDTO(playlistRepository.save(playlist));
    }

    // ── Converter entidade → DTO ─────────────────────────────────────────────────
    private PlaylistResponseDTO toDTO(Playlist playlist) {
        List<SongDTO> songs = playlist.getSongs().stream()
                .map(s -> new SongDTO(
                        s.getId(),
                        s.getName(),
                        s.getDuration(),
                        s.getImageUrl(),
                        s.getAudioUrl(),
                        s.getArtist() != null ? s.getArtist().getName() : null,
                        s.getArtist() != null ? s.getArtist().getId() : null
                ))
                .toList();

        return new PlaylistResponseDTO(
                playlist.getId(),
                playlist.getName(),
                playlist.getDescription(),
                playlist.getCoverUrl(),
                playlist.getOwner().getName(),
                playlist.getCreatedAt(),
                songs
        );
    }
}
