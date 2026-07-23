package com.example.demo.controller;

import com.example.demo.dto.PlaylistRequestDTO;
import com.example.demo.dto.PlaylistResponseDTO;
import com.example.demo.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    /**
     * POST /api/playlists
     * Cria uma nova playlist para o usuário autenticado.
     */
    @PostMapping
    public ResponseEntity<PlaylistResponseDTO> create(@RequestBody @Valid PlaylistRequestDTO dto) {
        return ResponseEntity.status(201).body(playlistService.create(dto));
    }

    /**
     * GET /api/playlists/public
     * Lista todas as playlists do sistema (Home).
     */
    @GetMapping("/public")
    public ResponseEntity<List<PlaylistResponseDTO>> findAllPublic() {
        return ResponseEntity.ok(playlistService.findAllPublicPlaylists());
    }

    /**
     * GET /api/playlists/public/{id}
     * Retorna uma playlist específica (Acesso Público).
     */
    @GetMapping("/public/{id}")
    public ResponseEntity<PlaylistResponseDTO> findPublicById(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.findByIdPublic(id));
    }

    /**
     * GET /api/playlists
     * Lista todas as playlists do usuário autenticado.
     */
    @GetMapping
    public ResponseEntity<List<PlaylistResponseDTO>> findMyPlaylists() {
        return ResponseEntity.ok(playlistService.findMyPlaylists());
    }

    /**
     * GET /api/playlists/{id}
     * Retorna uma playlist específica do usuário autenticado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.findById(id));
    }

    /**
     * PUT /api/playlists/{id}
     * Atualiza os dados de uma playlist do usuário autenticado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid PlaylistRequestDTO dto) {
        return ResponseEntity.ok(playlistService.update(id, dto));
    }

    /**
     * DELETE /api/playlists/{id}
     * Deleta uma playlist do usuário autenticado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playlistService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/playlists/{id}/songs/{songId}
     * Adiciona uma música à playlist.
     */
    @PostMapping("/{id}/songs/{songId}")
    public ResponseEntity<PlaylistResponseDTO> addSong(
            @PathVariable Long id,
            @PathVariable Long songId) {
        return ResponseEntity.ok(playlistService.addSong(id, songId));
    }

    /**
     * DELETE /api/playlists/{id}/songs/{songId}
     * Remove uma música da playlist.
     */
    @DeleteMapping("/{id}/songs/{songId}")
    public ResponseEntity<PlaylistResponseDTO> removeSong(
            @PathVariable Long id,
            @PathVariable Long songId) {
        return ResponseEntity.ok(playlistService.removeSong(id, songId));
    }
}
