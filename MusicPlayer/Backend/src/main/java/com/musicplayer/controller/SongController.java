package com.musicplayer.controller;

import com.musicplayer.dto.SongDTO;
import com.musicplayer.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "*")
public class SongController {
    
    private final SongService songService;
    
    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }
    
    @GetMapping
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        List<SongDTO> songs = songService.getAllSongs();
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> getSongById(@PathVariable Long id) {
        try {
            SongDTO song = songService.getSongById(id);
            return ResponseEntity.ok(song);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<SongDTO> createSong(@RequestBody SongDTO songDTO) {
        SongDTO createdSong = songService.createSong(songDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSong);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SongDTO> updateSong(@PathVariable Long id, @RequestBody SongDTO songDTO) {
        try {
            SongDTO updatedSong = songService.updateSong(id, songDTO);
            return ResponseEntity.ok(updatedSong);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        try {
            songService.deleteSong(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<SongDTO>> searchSongs(@RequestParam String query) {
        List<SongDTO> songs = songService.searchSongs(query);
        return ResponseEntity.ok(songs);
    }
}