package com.musicplayer.service;

import com.musicplayer.dto.PlaylistDTO;
import com.musicplayer.dto.SongDTO;
import com.musicplayer.model.Playlist;
import com.musicplayer.model.Song;
import com.musicplayer.repository.PlaylistRepository;
import com.musicplayer.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    
    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }
    
    @Transactional(readOnly = true)
    public List<PlaylistDTO> getAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PlaylistDTO getPlaylistById(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + id));
        return convertToDTO(playlist);
    }
    
    @Transactional
    public PlaylistDTO createPlaylist(PlaylistDTO playlistDTO) {
        Playlist playlist = new Playlist();
        playlist.setName(playlistDTO.getName());
        playlist.setDescription(playlistDTO.getDescription());
        playlist.setIcon(playlistDTO.getIcon());
        playlist.setCoverImage(playlistDTO.getCoverImage());
        playlist.setCreatedAt(java.time.LocalDateTime.now().toString());
        
        Playlist savedPlaylist = playlistRepository.save(playlist);
        return convertToDTO(savedPlaylist);
    }
    
    @Transactional
    public PlaylistDTO updatePlaylist(Long id, PlaylistDTO playlistDTO) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + id));
        
        playlist.setName(playlistDTO.getName());
        playlist.setDescription(playlistDTO.getDescription());
        playlist.setIcon(playlistDTO.getIcon());
        playlist.setCoverImage(playlistDTO.getCoverImage());
        
        Playlist updatedPlaylist = playlistRepository.save(playlist);
        return convertToDTO(updatedPlaylist);
    }
    
    @Transactional
    public void deletePlaylist(Long id) {
        if (!playlistRepository.existsById(id)) {
            throw new RuntimeException("Playlist not found with id: " + id);
        }
        playlistRepository.deleteById(id);
    }
    
    @Transactional
    public PlaylistDTO addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + playlistId));
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + songId));
        
        if (!playlist.getSongs().contains(song)) {
            playlist.getSongs().add(song);
            playlistRepository.save(playlist);
        }
        
        return convertToDTO(playlist);
    }
    
    @Transactional
    public PlaylistDTO removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + playlistId));
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + songId));
        
        playlist.getSongs().remove(song);
        playlistRepository.save(playlist);
        
        return convertToDTO(playlist);
    }
    
    private PlaylistDTO convertToDTO(Playlist playlist) {
        PlaylistDTO dto = new PlaylistDTO();
        dto.setId(playlist.getId());
        dto.setName(playlist.getName());
        dto.setDescription(playlist.getDescription());
        dto.setIcon(playlist.getIcon());
        dto.setCoverImage(playlist.getCoverImage()); // ADDED THIS LINE - This was missing!
        dto.setTrackCount(playlist.getTrackCount());
        
        List<SongDTO> songDTOs = playlist.getSongs().stream()
                .map(this::convertSongToDTO)
                .collect(Collectors.toList());
        dto.setSongs(songDTOs);
        
        return dto;
    }
    
    private SongDTO convertSongToDTO(Song song) {
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
}