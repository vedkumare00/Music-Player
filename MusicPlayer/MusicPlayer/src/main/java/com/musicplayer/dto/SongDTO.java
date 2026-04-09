package com.musicplayer.dto;

public class SongDTO {
    
    private Long id;
    private String title;
    private String artist;
    private Integer duration;
    private String albumArt;
    private String genre;
    private String filePath;
    
    public SongDTO() {}
    
    public SongDTO(Long id, String title, String artist, Integer duration, String albumArt, String genre, String filePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.albumArt = albumArt;
        this.genre = genre;
        this.filePath = filePath;
    }

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}