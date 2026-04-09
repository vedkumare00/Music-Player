package com.musicplayer.service;

import com.musicplayer.dto.SongDTO;
import com.musicplayer.model.Song;
import com.musicplayer.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongService {
    
    private final SongRepository songRepository;
    
    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }
    
    @Transactional(readOnly = true)
    public List<SongDTO> getAllSongs() {
        return songRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SongDTO> getRecentSongs() {
        // Get only 5 most recent songs based on creation date
        return songRepository.findAll().stream()
                .sorted((s1, s2) -> {
                    if (s2.getCreatedAt() != null && s1.getCreatedAt() != null) {
                        return s2.getCreatedAt().compareTo(s1.getCreatedAt());
                    }
                    return s2.getId().compareTo(s1.getId());
                })
                .limit(5)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public SongDTO getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + id));
        return convertToDTO(song);
    }
    
    @Transactional
    public SongDTO createSong(SongDTO songDTO) {
        Song song = convertToEntity(songDTO);
        song.setCreatedAt(java.time.LocalDateTime.now().toString());
        Song savedSong = songRepository.save(song);
        return convertToDTO(savedSong);
    }
    
    @Transactional
    public SongDTO updateSong(Long id, SongDTO songDTO) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + id));
        
        song.setTitle(songDTO.getTitle());
        song.setArtist(songDTO.getArtist());
        song.setDuration(songDTO.getDuration());
        song.setAlbumArt(songDTO.getAlbumArt());
        song.setGenre(songDTO.getGenre());
        song.setFilePath(songDTO.getFilePath());
        
        Song updatedSong = songRepository.save(song);
        return convertToDTO(updatedSong);
    }
    
    @Transactional
    public void deleteSong(Long id) {
        if (!songRepository.existsById(id)) {
            throw new RuntimeException("Song not found with id: " + id);
        }
        songRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<SongDTO> searchSongs(String query) {
        return songRepository.findByTitleContainingIgnoreCase(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private SongDTO convertToDTO(Song song) {
        SongDTO dto = new SongDTO();
        dto.setId(song.getId());
        dto.setTitle(song.getTitle());
        dto.setArtist(song.getArtist());
        dto.setDuration(song.getDuration());
        dto.setAlbumArt(song.getAlbumArt());
        dto.setGenre(song.getGenre());
        dto.setFilePath(song.getFilePath());
        return dto;
    }
    
    private Song convertToEntity(SongDTO dto) {
        Song song = new Song();
        song.setTitle(dto.getTitle());
        song.setArtist(dto.getArtist());
        song.setDuration(dto.getDuration());
        song.setAlbumArt(dto.getAlbumArt());
        song.setGenre(dto.getGenre());
        song.setFilePath(dto.getFilePath());
        return song;
    }
}