# SpotfyClone

A full-stack music streaming web application inspired by Spotify, built with React and Spring Boot.

**Live demo → [spotfy-clone-nine.vercel.app](https://spotfy-clone-nine.vercel.app)**

---

## Overview

SpotfyClone is a full-stack project that replicates the core experience of a music streaming platform. Users can browse artists and songs, create and manage personal playlists, and stream audio directly in the browser — all with a dark, modern interface inspired by Spotify's design language.

---

## Features

- Stream music with a persistent bottom player (play, pause, skip, volume control)
- Browse curated playlists, artists and songs on the home screen
- Create, edit and delete personal playlists
- Add and remove songs from playlists
- User authentication via email/password or Google (Firebase)
- JWT-based session management
- Redis caching for faster API responses

---

## Tech Stack

**Frontend**
- React 18 + Vite
- React Router DOM
- Vanilla CSS (custom design system)
- Firebase Authentication (Google OAuth)

**Backend**
- Java 21 + Spring Boot 4
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- PostgreSQL (Neon.tech)
- Redis (Upstash)

**Infrastructure**
- Frontend: Vercel
- Backend: Railway
- Database: Neon (serverless PostgreSQL)
- Cache: Upstash (serverless Redis)

---

## Getting Started

### Prerequisites

- Node.js 18+
- Java 21+
- Maven

### Backend

```bash
cd spring-backend
# Create src/main/resources/application-secret.properties with your credentials:
# DB_URL, DB_USER, DB_PASS, JWT_SECRET, REDIS_URL
.\mvnw.cmd spring-boot:run
```

### Frontend

```bash
cd playlist
npm install
npm run dev
```

The app will be available at `http://localhost:5173`.

---

## Environment Variables

### Backend (`application-secret.properties`)

```properties
DB_URL=jdbc:postgresql://<host>/neondb?sslmode=require
DB_USER=<username>
DB_PASS=<password>
JWT_SECRET=<your-secret>
REDIS_URL=rediss://<upstash-url>
```

### Frontend (`.env`)

```env
VITE_API_URL=https://<your-railway-url>.up.railway.app
```

---

## Project Structure

```
SpotfyClone/
├── playlist/               # React frontend
│   ├── src/
│   │   ├── components/     # Reusable UI components
│   │   ├── contexts/       # Auth and Playlist context
│   │   ├── pages/          # Route-level pages
│   │   └── services/       # API and Firebase config
│   └── vercel.json
└── spring-backend/         # Spring Boot API
    └── src/main/java/com/example/demo/
        ├── controller/
        ├── service/
        ├── repository/
        ├── dto/
        └── config/
```
