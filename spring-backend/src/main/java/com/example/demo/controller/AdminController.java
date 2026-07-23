package com.example.demo.controller;

import com.example.demo.model.Song;
import com.example.demo.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Endpoint temporário de manutenção — atualiza audioUrl de músicas.
 * Usado pelo script de migração Deezer. Pode ser removido após o seed.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private SongRepository songRepository;

    @PatchMapping("/songs/{id}/audio")
    public ResponseEntity<?> updateAudioUrl(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String newAudioUrl = body.get("audioUrl");
        if (newAudioUrl == null || newAudioUrl.isBlank()) {
            return ResponseEntity.badRequest().body("audioUrl is required");
        }

        return songRepository.findById(id)
                .map(song -> {
                    song.setAudioUrl(newAudioUrl);
                    songRepository.save(song);
                    return ResponseEntity.ok(Map.of(
                            "id", id,
                            "name", song.getName(),
                            "audioUrl", newAudioUrl
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
