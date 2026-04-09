// API Configuration
const API_BASE = 'http://localhost:8080/api';

// Player state - Use sessionStorage to persist across pages
let songs = [];
let currentPlaylistSongs = []; // Songs in current playlist context
let playlists = [];
let currentSongIndex = 0;
let isPlaying = false;
let isPlaylistMode = false; // Track if we're playing from a playlist

// Get elements
const audioPlayer = new Audio();
const playPauseBtn = document.getElementById('playPauseBtn');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const progressBar = document.querySelector('.progress-bar');
const progress = document.getElementById('progress');
const currentTimeEl = document.getElementById('currentTime');
const durationEl = document.getElementById('duration');
const songTitle = document.querySelector('.player-info .song-title');
const songArtist = document.querySelector('.player-info .song-artist');

// Get active song list (playlist or all songs)
function getActiveSongList() {
    return isPlaylistMode && currentPlaylistSongs.length > 0 ? currentPlaylistSongs : songs;
}

// Track recently played songs
function addToRecentlyPlayed(songId) {
    let recentSongs = JSON.parse(localStorage.getItem('recentlyPlayed') || '[]');
    recentSongs = recentSongs.filter(id => id !== songId);
    recentSongs.unshift(songId);
    recentSongs = recentSongs.slice(0, 5);
    localStorage.setItem('recentlyPlayed', JSON.stringify(recentSongs));
}

// Get recently played songs
function getRecentlyPlayedSongs() {
    const recentIds = JSON.parse(localStorage.getItem('recentlyPlayed') || '[]');
    return recentIds
        .map(id => songs.find(s => s.id === id))
        .filter(s => s != null);
}

// Save player state to sessionStorage
function savePlayerState() {
    const state = {
        currentSongIndex: currentSongIndex,
        isPlaying: isPlaying,
        currentTime: audioPlayer.currentTime,
        songs: songs,
        isPlaylistMode: isPlaylistMode,
        currentPlaylistSongs: currentPlaylistSongs
    };
    sessionStorage.setItem('playerState', JSON.stringify(state));
}

// Load player state from sessionStorage
function loadPlayerState() {
    const savedState = sessionStorage.getItem('playerState');
    if (savedState) {
        const state = JSON.parse(savedState);
        songs = state.songs || [];
        currentPlaylistSongs = state.currentPlaylistSongs || [];
        isPlaylistMode = state.isPlaylistMode || false;
        currentSongIndex = state.currentSongIndex || 0;
        isPlaying = state.isPlaying || false;
        
        const activeSongs = getActiveSongList();
        if (activeSongs.length > 0 && currentSongIndex < activeSongs.length) {
            loadSong(currentSongIndex, false);
            audioPlayer.currentTime = state.currentTime || 0;
            
            if (isPlaying) {
                setTimeout(() => play(), 100);
            }
        }
    }
}

// Check if we're on home page
function isHomePage() {
    return window.location.pathname.includes('index.html') || 
           window.location.pathname === '/' ||
           window.location.pathname.endsWith('/');
}

// Fetch songs from API
async function fetchSongs() {
    try {
        const response = await fetch(`${API_BASE}/songs`, {
            cache: 'no-cache'
        });
        songs = await response.json();
        console.log('Loaded all songs:', songs);
        
        if (!sessionStorage.getItem('playerState')) {
            savePlayerState();
        }
        
        // Render tracks based on page
        if (isHomePage()) {
            isPlaylistMode = false;
            currentPlaylistSongs = [];
            const recentSongs = getRecentlyPlayedSongs();
            if (recentSongs.length > 0) {
                console.log('Showing recently played songs:', recentSongs);
                renderTracks(recentSongs);
            } else {
                console.log('No recent history, showing first 4 songs');
                renderTracks(songs.slice(0, 4));
            }
        } else {
            renderTracks();
        }
        
        const savedState = sessionStorage.getItem('playerState');
        if (savedState) {
            loadPlayerState();
        } else if (songs.length > 0) {
            loadSong(0, false);
        }
    } catch (error) {
        console.error('Error fetching songs:', error);
        songTitle.textContent = 'Error loading songs';
        songArtist.textContent = 'Check if backend is running';
    }
}

// Fetch playlists from API
async function fetchPlaylists() {
    try {
        const response = await fetch(`${API_BASE}/playlists`, {
            cache: 'no-cache'
        });
        playlists = await response.json();
        console.log('Loaded playlists:', playlists);
        renderPlaylists();
    } catch (error) {
        console.error('Error fetching playlists:', error);
    }
}


// Render playlists with image support - FIXED VERSION
function renderPlaylists() {
    const playlistsContainer = document.querySelector('.playlists');
    if (!playlistsContainer) return;
    
    playlistsContainer.innerHTML = '';
    
    playlists.forEach(playlist => {
        const playlistCard = document.createElement('div');
        playlistCard.className = 'playlist-card';
        playlistCard.dataset.playlistId = playlist.id;
        
        // Check if playlist has a cover image
        let coverHTML;
        if (playlist.coverImage && playlist.coverImage.trim() !== '') {
            // If there's a cover image URL, use it with error handling
            coverHTML = `
                <img src="${playlist.coverImage}" 
                     alt="${playlist.name}" 
                     class="playlist-cover"
                     onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                <div class="playlist-icon" style="display: none;">
                    ${playlist.icon || '🎵'}
                </div>
            `;
        } else {
            // Otherwise use the icon/emoji
            coverHTML = `
                <div class="playlist-icon">
                    ${playlist.icon || '🎵'}
                </div>
            `;
        }
        
        playlistCard.innerHTML = `
            ${coverHTML}
            <div class="playlist-details">
                <h3>${playlist.name}</h3>
                <p>${playlist.trackCount || 0} tracks</p>
            </div>
        `;
        
        playlistCard.addEventListener('click', function() {
            const playlistId = this.dataset.playlistId;
            loadPlaylistSongs(playlistId);
        });
        
        playlistsContainer.appendChild(playlistCard);
    });
}

// Load songs for a specific playlist
async function loadPlaylistSongs(playlistId) {
    try {
        const response = await fetch(`${API_BASE}/playlists/${playlistId}`, {
            cache: 'no-cache'
        });
        const playlist = await response.json();
        
        console.log('Loaded playlist:', playlist);
        
        // Set playlist mode and current playlist songs
        isPlaylistMode = true;
        currentPlaylistSongs = playlist.songs || [];
        
        const playlistTracksSection = document.querySelector('.playlist-tracks');
        if (playlistTracksSection) {
            const heading = playlistTracksSection.querySelector('h2');
            if (heading) {
                heading.textContent = playlist.name;
            }
            
            const trackList = playlistTracksSection.querySelector('.track-list');
            if (trackList) {
                trackList.innerHTML = '';
                
                if (currentPlaylistSongs.length === 0) {
                    trackList.innerHTML = '<p style="color: #888; padding: 20px;">No songs in this playlist yet.</p>';
                } else {
                    currentPlaylistSongs.forEach((song, index) => {
                        const trackItem = document.createElement('div');
                        trackItem.className = 'track-item';
                        trackItem.dataset.playlistIndex = index;
                        trackItem.innerHTML = `
                            <div class="track-info">
                                <div class="track-title">${song.title}</div>
                                <div class="track-artist">${song.artist}</div>
                            </div>
                            <button class="play-btn">▶</button>
                        `;
                        
                        trackItem.addEventListener('click', function() {
                            const playlistIndex = parseInt(this.dataset.playlistIndex);
                            if (currentSongIndex === playlistIndex && isPlaying) {
                                pause();
                            } else {
                                loadSong(playlistIndex);
                                play();
                            }
                        });
                        
                        trackList.appendChild(trackItem);
                    });
                }
            }
        }
        
        savePlayerState();
    } catch (error) {
        console.error('Error loading playlist songs:', error);
    }
}

// Render tracks dynamically
function renderTracks(songsToRender = songs) {
    const trackList = document.querySelector('.track-list');
    if (!trackList) return;
    
    trackList.innerHTML = '';
    
    songsToRender.forEach((song, index) => {
        const trackItem = document.createElement('div');
        trackItem.className = 'track-item';
        trackItem.dataset.song = index;
        trackItem.innerHTML = `
            <div class="track-info">
                <div class="track-title">${song.title}</div>
                <div class="track-artist">${song.artist}</div>
            </div>
            <button class="play-btn">▶</button>
        `;
        
        trackItem.addEventListener('click', function() {
            // When clicking from home page, disable playlist mode
            isPlaylistMode = false;
            currentPlaylistSongs = [];
            
            const songIndex = parseInt(this.dataset.song);
            if (currentSongIndex === songIndex && isPlaying) {
                pause();
            } else {
                loadSong(songIndex);
                play();
            }
        });
        
        trackList.appendChild(trackItem);
    });
}

// Load song
function loadSong(index, autoSave = true) {
    const activeSongs = getActiveSongList();
    
    if (index < 0 || index >= activeSongs.length) return;
    
    currentSongIndex = index;
    const song = activeSongs[currentSongIndex];
    
    songTitle.textContent = song.title;
    songArtist.textContent = song.artist;
    
    if (song.filePath) {
        audioPlayer.src = `${API_BASE}/audio/stream/${song.filePath}`;
    } else {
        console.warn('No file path for song:', song.title);
        songTitle.textContent = 'No audio file available';
    }
    
    updateTrackHighlight();
    
    if (autoSave) {
        savePlayerState();
    }
}

// Play song
function play() {
    audioPlayer.play()
        .then(() => {
            isPlaying = true;
            playPauseBtn.textContent = '⏸';
            updateTrackHighlight();
            savePlayerState();
            
            // Track this as recently played
            const activeSongs = getActiveSongList();
            if (activeSongs[currentSongIndex]) {
                addToRecentlyPlayed(activeSongs[currentSongIndex].id);
                console.log('Added to recently played:', activeSongs[currentSongIndex].title);
            }
        })
        .catch(error => {
            console.error('Error playing audio:', error);
            songArtist.textContent = 'Error playing audio';
        });
}

// Pause song
function pause() {
    audioPlayer.pause();
    isPlaying = false;
    playPauseBtn.textContent = '▶';
    updateTrackHighlight();
    savePlayerState();
}

// Next song
function next() {
    const activeSongs = getActiveSongList();
    currentSongIndex = (currentSongIndex + 1) % activeSongs.length;
    loadSong(currentSongIndex);
    if (isPlaying) {
        play();
    }
}

// Previous song
function prev() {
    if (audioPlayer.currentTime > 3) {
        audioPlayer.currentTime = 0;
    } else {
        const activeSongs = getActiveSongList();
        currentSongIndex = (currentSongIndex - 1 + activeSongs.length) % activeSongs.length;
        loadSong(currentSongIndex);
        if (isPlaying) {
            play();
        }
    }
}

// Update progress bar
function updateProgress() {
    if (audioPlayer.duration) {
        const percent = (audioPlayer.currentTime / audioPlayer.duration) * 100;
        progress.style.width = percent + '%';
        currentTimeEl.textContent = formatTime(audioPlayer.currentTime);
        durationEl.textContent = formatTime(audioPlayer.duration);
    }
}

// Update track highlight
function updateTrackHighlight() {
    const trackItems = document.querySelectorAll('.track-item');
    trackItems.forEach((item) => {
        const playBtn = item.querySelector('.play-btn');
        
        // Check both regular song index and playlist index
        const itemIndex = item.dataset.song ? parseInt(item.dataset.song) : 
                         item.dataset.playlistIndex ? parseInt(item.dataset.playlistIndex) : -1;
        
        if (itemIndex === currentSongIndex && isPlaying) {
            item.classList.add('playing');
            playBtn.textContent = '⏸';
        } else {
            item.classList.remove('playing');
            playBtn.textContent = '▶';
        }
    });
}

// Format time
function formatTime(seconds) {
    if (isNaN(seconds)) return '0:00';
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return `${mins}:${secs.toString().padStart(2, '0')}`;
}

// Event listeners
if (playPauseBtn) {
    playPauseBtn.addEventListener('click', () => {
        if (isPlaying) {
            pause();
        } else {
            play();
        }
    });
}

if (nextBtn) nextBtn.addEventListener('click', next);
if (prevBtn) prevBtn.addEventListener('click', prev);

// Audio player events
audioPlayer.addEventListener('timeupdate', () => {
    updateProgress();
    savePlayerState();
});

audioPlayer.addEventListener('ended', next);

audioPlayer.addEventListener('loadedmetadata', () => {
    durationEl.textContent = formatTime(audioPlayer.duration);
});

// Progress bar click
if (progressBar) {
    progressBar.addEventListener('click', (e) => {
        const rect = progressBar.getBoundingClientRect();
        const percent = (e.clientX - rect.left) / rect.width;
        audioPlayer.currentTime = percent * audioPlayer.duration;
    });
}

// Save state before page unload
window.addEventListener('beforeunload', () => {
    savePlayerState();
});

// Initialize on load
if (window.location.pathname.includes('playlist.html')) {
    fetchPlaylists();
    fetchSongs();
} else {
    fetchSongs();
}