package com.example.demo.service;

import com.example.demo.dto.SongDTO;
import com.example.demo.model.Song;
import com.example.demo.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.cache.annotation.Cacheable;

import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Cacheable("songs")
    public List<SongDTO> findAll() {
        return songRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public SongDTO findById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Música não encontrada: id=" + id));
        return toDTO(song);
    }

    public List<SongDTO> findByArtist(Long artistId) {
        return songRepository.findByArtistId(artistId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<SongDTO> search(String query) {
        return songRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Busca um link de preview válido (30s) diretamente da API da Deezer em tempo real.
     */
    public String getFreshAudioUrl(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Música não encontrada: id=" + id));

        String artistName = song.getArtist() != null ? song.getArtist().getName() : "";
        // iTunes Search API — gratuita, sem bloqueio de CDN
        String term = java.net.URLEncoder.encode(song.getName() + " " + artistName, java.nio.charset.StandardCharsets.UTF_8);
        String itunesUrl = "https://itunes.apple.com/search?term=" + term + "&entity=musicTrack&limit=5";

        try {
            org.springframework.http.client.SimpleClientHttpRequestFactory factory =
                    new org.springframework.http.client.SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(3000);
            factory.setReadTimeout(5000);
            RestTemplate restTemplate = new RestTemplate(factory);

            Map response = restTemplate.getForObject(itunesUrl, Map.class);
            if (response != null && response.containsKey("results")) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                if (results != null) {
                    for (Map<String, Object> track : results) {
                        String preview = (String) track.get("previewUrl");
                        if (preview != null && !preview.isBlank()) {
                            System.out.println("iTunes preview encontrado: " + preview);
                            return preview;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Falha ao buscar áudio no iTunes: " + e.getMessage());
        }

        // Fallback: URL salva no banco
        return song.getAudioUrl();
    }

    private SongDTO toDTO(Song song) {
        String artistName = song.getArtist() != null ? song.getArtist().getName() : null;
        Long artistId = song.getArtist() != null ? song.getArtist().getId() : null;
        
        // Em vez de retornar a URL do banco (que expira), retornamos a rota do nosso Proxy
        String proxyUrl = "/api/songs/" + song.getId() + "/audio";
        
        return new SongDTO(
                song.getId(),
                song.getName(),
                song.getDuration(),
                song.getImageUrl(),
                proxyUrl,
                artistName,
                artistId
        );
    }
}
