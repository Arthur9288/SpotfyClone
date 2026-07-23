package com.example.demo.controller;

import com.example.demo.dto.ArtistDTO;
import com.example.demo.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    /**
     * GET /api/artists
     * Rota pública — lista todos os artistas.
     */
    @GetMapping
    public ResponseEntity<List<ArtistDTO>> findAll() {
        return ResponseEntity.ok(artistService.findAll());
    }

    /**
     * GET /api/artists/{id}
     * Rota pública — busca artista por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(artistService.findById(id));
    }
}
