# 🎵 Music Player

A full-stack music streaming web application built with **Spring Boot**, **MySQL**, and **vanilla JavaScript**. Browse songs, organize them into playlists, and stream audio directly from your local music library through a clean, minimal dark-themed UI.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

---

## ✨ Features

- 🎧 Stream audio files directly from a local music directory
- 📃 Create, update, and delete playlists with custom cover images or emoji icons
- ➕ Add / remove songs from playlists
- 🔍 Search songs by title
- ⏯️ Full playback controls — play, pause, next, previous, seek
- 🕒 "Recently Played" tracking (persisted via `localStorage`)
- 💾 Player state persists across page navigation (via `sessionStorage`)
- 🎨 Responsive, minimal dark-mode UI

---

## 🏗️ Tech Stack

**Backend**
- Java 17
- Spring Boot 3 (Web, Data JPA)
- MySQL 8
- Hibernate / JPA
- Maven

**Frontend**
- HTML5, CSS3
- Vanilla JavaScript (ES6+, Fetch API)

---

## 📂 Project Structure

```
MusicPlayer/
├── src/main/java/com/musicplayer/
│   ├── config/
│   │   ├── CorsConfig.java
│   │   └── DataInitializer.java
│   ├── controller/
│   │   ├── AudioStreamController.java
│   │   ├── PlaylistController.java
│   │   └── SongController.java
│   ├── dto/
│   │   ├── PlaylistDTO.java
│   │   └── SongDTO.java
│   ├── exception/
│   │   └── GlobalExceptionHandler.java
│   ├── model/
│   │   ├── Playlist.java
│   │   └── Song.java
│   ├── repository/
│   │   ├── PlaylistRepository.java
│   │   └── SongRepository.java
│   ├── service/
│   │   ├── PlaylistService.java
│   │   └── SongService.java
│   └── MusicPlayerApplication.java
├── src/main/resources/
│   └── application.properties
├── index.html
├── playlist.html
├── style.css
└── script.js
```

---

## ⚙️ Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8.x running locally
- A modern web browser
- (Optional) VS Code Live Server or any static file server for the frontend

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/vedkumare00/music-player.git
cd MusicPlayer
```

### 2. Configure the database

Create a MySQL database:

```sql
CREATE DATABASE musicdb;
```

Update `src/main/resources/application.properties` with your own credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/musicdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Configure your music folder

`AudioStreamController.java` currently points to a hardcoded local path:

```java
private final String MUSIC_DIRECTORY = "C:\\Java-Miniproject-2025\\Music\\";
```

Update this to wherever your `.mp3` files live, or (better) externalize it into `application.properties`:

```properties
music.directory=C:/path/to/your/Music/
```

```java
@Value("${music.directory}")
private String MUSIC_DIRECTORY;
```

### 4. Run the backend

```bash
mvn spring-boot:run
```

The API will start on `http://localhost:8080`. On first run, `DataInitializer` seeds the database with sample playlists and songs.

### 5. Run the frontend

Serve `index.html` with any static server, e.g. VS Code's **Live Server** extension, so it runs on `http://127.0.0.1:5500` (already whitelisted in CORS config).

---

## 🔌 API Endpoints

### Songs — `/api/songs`
| Method | Endpoint              | Description             |
|--------|------------------------|--------------------------|
| GET    | `/api/songs`            | Get all songs           |
| GET    | `/api/songs/{id}`       | Get a song by ID        |
| POST   | `/api/songs`            | Create a new song        |
| PUT    | `/api/songs/{id}`       | Update a song            |
| DELETE | `/api/songs/{id}`       | Delete a song            |
| GET    | `/api/songs/search?query=` | Search songs by title |

### Playlists — `/api/playlists`
| Method | Endpoint                                       | Description                    |
|--------|--------------------------------------------------|---------------------------------|
| GET    | `/api/playlists`                                 | Get all playlists              |
| GET    | `/api/playlists/{id}`                            | Get a playlist by ID           |
| POST   | `/api/playlists`                                 | Create a new playlist          |
| PUT    | `/api/playlists/{id}`                            | Update a playlist              |
| DELETE | `/api/playlists/{id}`                            | Delete a playlist              |
| POST   | `/api/playlists/{playlistId}/songs/{songId}`     | Add a song to a playlist       |
| DELETE | `/api/playlists/{playlistId}/songs/{songId}`     | Remove a song from a playlist  |

### Audio — `/api/audio`
| Method | Endpoint                       | Description                  |
|--------|----------------------------------|-------------------------------|
| GET    | `/api/audio/stream/{filename}`  | Stream an audio file by name |

---

## 🗄️ Database Schema

**playlists**
| Column       | Type    |
|--------------|---------|
| id           | BIGINT (PK) |
| name         | VARCHAR |
| description  | VARCHAR |
| icon         | VARCHAR |
| cover_image  | VARCHAR |
| created_at   | VARCHAR |

**songs**
| Column       | Type    |
|--------------|---------|
| id           | BIGINT (PK) |
| title        | VARCHAR |
| artist       | VARCHAR |
| duration     | INT     |
| file_path    | VARCHAR |
| album_art    | VARCHAR |
| genre        | VARCHAR |
| created_at   | VARCHAR |
| play_count   | INT     |
| playlist_id  | BIGINT (FK → playlists.id) |

Schema is auto-generated by Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

---

## 🔒 Security Notes (before you push)

A few things in the current codebase are fine for local development but **should be tightened before deploying or open-sourcing**:

- `application.properties` contains a plaintext DB password — move secrets to environment variables or a `.gitignore`'d `application-local.properties` file.
- `CorsConfig.java` and each controller allow `origins = "*"` — restrict this to your actual frontend origin(s) in production.
- `AudioStreamController` builds file paths from user input; the `.normalize()` call helps, but double-check it can't be used to traverse outside `MUSIC_DIRECTORY`.
- The hardcoded Windows path (`C:\Java-Miniproject-2025\Music\`) is machine-specific — externalize it as shown above so the project runs on any machine.

Add a `.gitignore` with at least:

```
target/
*.class
application-local.properties
.idea/
*.iml
```

---

## 🛣️ Roadmap / Ideas

- [ ] User authentication and per-user playlists
- [ ] Album art upload support
- [ ] Shuffle and repeat modes
- [ ] Volume control
- [ ] Deploy backend (Render/Railway) + frontend (Netlify/Vercel)

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 🙌 Acknowledgements

Built as a mini-project exploring full-stack development with Spring Boot and vanilla JS.
