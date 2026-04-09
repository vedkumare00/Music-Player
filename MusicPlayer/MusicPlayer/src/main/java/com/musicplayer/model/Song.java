package com.musicplayer.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "songs")
public class Song {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String artist;
    
    @Column(nullable = false)
    private Integer duration;
    
    private String filePath;
    
    private String albumArt;
    
    private String genre;
    
    private String createdAt;
    
    @Column(name = "play_count")
    private Integer playCount = 0;
    
    @ManyToOne
    @JoinColumn(name = "playlist_id")
    @JsonIgnore
    private Playlist playlist;

    // Constructors
    public Song() {
    }

    public Song(Long id, String title, String artist, Integer duration, String filePath, 
                String albumArt, String genre, String createdAt, Integer playCount, Playlist playlist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.filePath = filePath;
        this.albumArt = albumArt;
        this.genre = genre;
        this.createdAt = createdAt;
        this.playCount = playCount;
        this.playlist = playlist;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}