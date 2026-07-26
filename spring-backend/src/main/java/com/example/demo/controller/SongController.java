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
     * Proxy real: baixa o áudio do Deezer no servidor e repassa ao browser.
     * Evita bloqueio da CDN Akamai que ocorre com redirect 302.
     */
    @GetMapping("/{id}/audio")
    public ResponseEntity<byte[]> getAudioStream(@PathVariable Long id) {
        String freshUrl = songService.getFreshAudioUrl(id);
        try {
            org.springframework.http.client.SimpleClientHttpRequestFactory factory =
                    new org.springframework.http.client.SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(3000);
            factory.setReadTimeout(10000);

            org.springframework.web.client.RestTemplate restTemplate =
                    new org.springframework.web.client.RestTemplate(factory);

            // Monta o request com User-Agent de browser para não ser bloqueado pela CDN
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120.0 Safari/537.36");
            org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);

            ResponseEntity<byte[]> deezerResponse = restTemplate.exchange(
                    freshUrl,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    byte[].class
            );

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType("audio/mpeg"))
                    .header("Accept-Ranges", "bytes")
                    .header("Cache-Control", "no-cache")
                    .body(deezerResponse.getBody());

        } catch (Exception e) {
            System.err.println("Erro ao fazer proxy do áudio: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
