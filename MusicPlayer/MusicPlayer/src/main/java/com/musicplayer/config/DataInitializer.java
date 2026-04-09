package com.musicplayer.config;

import com.musicplayer.model.Playlist;
import com.musicplayer.model.Song;
import com.musicplayer.repository.PlaylistRepository;
import com.musicplayer.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    
    @Autowired
    public DataInitializer(SongRepository songRepository, PlaylistRepository playlistRepository) {
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
    }
    
    @Override
    public void run(String... args) {
        if (songRepository.count() > 0) {
            System.out.println("Database already initialized. Skipping data insertion.");
            return;
        }

        System.out.println("Initializing database with sample data...");

        // Create playlists with cover images (using placeholder images or local paths)
        Playlist chillVibes = new Playlist();
        chillVibes.setName("Chill Vibes");
        chillVibes.setDescription("Relaxing tracks for any time");
        chillVibes.setIcon("🎵");
        // You can use:
        // 1. Local image path: "/images/chill-vibes.jpg"
        // 2. External URL: "https://via.placeholder.com/300x300/4A90E2/ffffff?text=Chill+Vibes"
        // 3. Or leave null to use emoji icon
        chillVibes.setCoverImage("https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=300&h=300&fit=crop");
        chillVibes.setCreatedAt(LocalDateTime.now().toString());
        
        Playlist southIndian = new Playlist();
        southIndian.setName("South Indian Songs");
        southIndian.setDescription("Best of South Indian music");
        southIndian.setIcon("🎸");
        southIndian.setCoverImage("https://images.unsplash.com/photo-1514320291840-2e0a9bf2a9ae?w=300&h=300&fit=crop");
        southIndian.setCreatedAt(LocalDateTime.now().toString());
        
        Playlist nineties = new Playlist();
        nineties.setName("90s Songs");
        nineties.setDescription("Nostalgic hits from the 90s");
        nineties.setIcon("🎹");
        nineties.setCoverImage("https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=300&h=300&fit=crop");
        nineties.setCreatedAt(LocalDateTime.now().toString());
        
        Playlist marathi = new Playlist();
        marathi.setName("Marathi Songs");
        marathi.setDescription("Beautiful Marathi collection");
        marathi.setIcon("🎧");
        marathi.setCoverImage("https://images.unsplash.com/photo-1511379938547-c1f69419868d?w=300&h=300&fit=crop");
        marathi.setCreatedAt(LocalDateTime.now().toString());

        // Save playlists
        chillVibes = playlistRepository.save(chillVibes);
        southIndian = playlistRepository.save(southIndian);
        nineties = playlistRepository.save(nineties);
        marathi = playlistRepository.save(marathi);

        // Create songs
        Song song1 = new Song();
        song1.setTitle("Zaroor");
        song1.setFilePath("Zaroor.mp3");
        song1.setArtist("Aparshakti Khurana");
        song1.setDuration(243);
        song1.setGenre("Chill");
        song1.setCreatedAt(LocalDateTime.now().toString());
        song1.setPlaylist(chillVibes);
        
        Song song2 = new Song();
        song2.setTitle("Blue Eyes");
        song2.setFilePath("BLue_eyes.mp3");
        song2.setArtist("Yo Yo Honey Singh");
        song2.setDuration(198);
        song2.setGenre("Chill");
        song2.setCreatedAt(LocalDateTime.now().toString());
        song2.setPlaylist(chillVibes);
        
        Song song3 = new Song();
        song3.setTitle("Sapphire");
        song3.setFilePath("Sapphire.mp3");
        song3.setArtist("Ed Sheren");
        song3.setDuration(267);
        song3.setGenre("Electronic");
        song3.setCreatedAt(LocalDateTime.now().toString());
        song3.setPlaylist(chillVibes);
        
        Song song4 = new Song();
        song4.setTitle("Tere Pyar Mein");
        song4.setFilePath("Tere_pyar_mai.mp3");
        song4.setArtist("Arijit Singh");
        song4.setDuration(215);
        song4.setGenre("Chill");
        song4.setCreatedAt(LocalDateTime.now().toString());
        song4.setPlaylist(chillVibes);
        
        // Save all songs
        songRepository.saveAll(Arrays.asList(song1, song2, song3, song4));
        
        System.out.println("Database initialized with sample data successfully!");
        System.out.println("Total Playlists: " + playlistRepository.count());
        System.out.println("Total Songs: " + songRepository.count());
    }
}