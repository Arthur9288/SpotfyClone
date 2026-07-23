package com.example.demo.controller;

import com.example.demo.dto.SongDTO;
import com.example.demo.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    @Autowired
    private SongService songService;

    /**
     * GET /api/songs
     * Rota pública — lista todas as músicas.
     * Suporta filtro: /api/songs?artistId=1 ou /api/songs?q=creep
     */
    @GetMapping
    public ResponseEntity<List<SongDTO>> findAll(
            @RequestParam(required = false) Long artistId,
            @RequestParam(name = "q", required = false) String query) {

        if (artistId != null) {
            return ResponseEntity.ok(songService.findByArtist(artistId));
        }
        if (query != null && !query.isBlank()) {
            return ResponseEntity.ok(songService.search(query));
        }
        return ResponseEntity.ok(songService.findAll());
    }

    /**
     * GET /api/songs/{id}
     * Rota pública — busca música por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(songService.findById(id));
    }

    /**
     * GET /api/songs/{id}/audio
     * Proxy dinâmico: busca a música na Deezer em tempo real e redireciona (302) o player de áudio.
     */
    @GetMapping("/{id}/audio")
    public ResponseEntity<Void> getAudioStream(@PathVariable Long id) {
        String freshUrl = songService.getFreshAudioUrl(id);
        return ResponseEntity.status(org.springframework.http.HttpStatus.FOUND)
                .location(java.net.URI.create(freshUrl))
                .build();
    }
}
