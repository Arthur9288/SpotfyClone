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
        String query = "track:\"" + song.getName() + "\" artist:\"" + artistName + "\"";
        String deezerUrl = "https://api.deezer.com/search?q=" + query;
        
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map response = restTemplate.getForObject(deezerUrl, Map.class);
            if (response != null && response.containsKey("data")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
                if (data != null && !data.isEmpty()) {
                    String preview = (String) data.get(0).get("preview");
                    if (preview != null && !preview.isBlank()) {
                        return preview;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Falha ao buscar áudio na Deezer: " + e.getMessage());
        }
        
        // Fallback: retorna a URL que está no banco caso a Deezer falhe
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
