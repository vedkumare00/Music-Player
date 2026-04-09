package com.musicplayer.controller;

import com.musicplayer.dto.PlaylistDTO;
import com.musicplayer.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "*")
public class PlaylistController {
    
    private final PlaylistService playlistService;
    
    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }
    
    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> getAllPlaylists() {
        List<PlaylistDTO> playlists = playlistService.getAllPlaylists();
        return ResponseEntity.ok(playlists);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDTO> getPlaylistById(@PathVariable Long id) {
        try {
            PlaylistDTO playlist = playlistService.getPlaylistById(id);
            return ResponseEntity.ok(playlist);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<PlaylistDTO> createPlaylist(@RequestBody PlaylistDTO playlistDTO) {
        PlaylistDTO createdPlaylist = playlistService.createPlaylist(playlistDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlaylist);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDTO> updatePlaylist(@PathVariable Long id, @RequestBody PlaylistDTO playlistDTO) {
        try {
            PlaylistDTO updatedPlaylist = playlistService.updatePlaylist(id, playlistDTO);
            return ResponseEntity.ok(updatedPlaylist);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        try {
            playlistService.deletePlaylist(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<PlaylistDTO> addSongToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        try {
            PlaylistDTO playlist = playlistService.addSongToPlaylist(playlistId, songId);
            return ResponseEntity.ok(playlist);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<PlaylistDTO> removeSongFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        try {
            PlaylistDTO playlist = playlistService.removeSongFromPlaylist(playlistId, songId);
            return ResponseEntity.ok(playlist);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}