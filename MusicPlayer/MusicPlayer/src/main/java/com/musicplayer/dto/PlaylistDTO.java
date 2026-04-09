package com.musicplayer.dto;

import java.util.List;

public class PlaylistDTO {
    
    private Long id;
    private String name;
    private String description;
    private String icon;
    private String coverImage;
    private Integer trackCount;
    private List<SongDTO> songs;
    
    public PlaylistDTO() {}
    
    public PlaylistDTO(Long id, String name, String description, String icon, String coverImage, Integer trackCount, List<SongDTO> songs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.coverImage = coverImage;
        this.trackCount = trackCount;
        this.songs = songs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    public List<SongDTO> getSongs() {
        return songs;
    }

    public void setSongs(List<SongDTO> songs) {
        this.songs = songs;
    }
}