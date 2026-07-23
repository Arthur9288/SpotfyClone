/**
 * API Service Layer
 * ─────────────────
 * All HTTP calls to the Spring Boot backend live here.
 * When your backend is ready, set:
 *   VITE_API_URL=http://localhost:8080
 * in a .env file at the root of /playlist.
 *
 * The JWT token is automatically attached to every request.
 */

// Em DEV: usa proxy do Vite (vite.config.js) → sem CORS
// Em PROD: defina VITE_API_URL=https://seu-backend.railway.app no .env
const BASE_URL = import.meta.env.VITE_API_URL ?? "";

// ─── Core fetch wrapper ──────────────────────────────────────────────────────
const request = async (method, path, body = null) => {
  const token = localStorage.getItem("spotify_token");

  const headers = { "Content-Type": "application/json" };
  if (token) headers["Authorization"] = `Bearer ${token}`;

  const res = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : null,
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({ message: "Erro desconhecido" }));
    throw new Error(err.error ?? err.message ?? `HTTP ${res.status}`);
  }

  if (res.status === 204) return null;
  const text = await res.text();
  return text ? JSON.parse(text) : null;
};

// ─── Auth ────────────────────────────────────────────────────────────────────
export const authApi = {
  register: (name, email, password) =>
    request("POST", "/auth/register", { name, email, password }),

  login: (email, password) =>
    request("POST", "/auth/login", { email, password }),

  loginWithGoogle: (token) =>
    request("POST", "/auth/google", { token }),
};

// ─── Songs ───────────────────────────────────────────────────────────────────
export const songsApi = {
  getAll:   ()   => request("GET",  "/api/songs"),
  getById:  (id) => request("GET",  `/api/songs/${id}`),
  search:   (q)  => request("GET",  `/api/songs?q=${encodeURIComponent(q)}`),
};

// ─── Artists ─────────────────────────────────────────────────────────────────
export const artistsApi = {
  getAll:  ()   => request("GET", "/api/artists"),
  getById: (id) => request("GET", `/api/artists/${id}`),
};

// ─── Playlists ───────────────────────────────────────────────────────────────
export const playlistsApi = {
  getAll: () => request("GET", "/api/playlists"),

  getById: (id) => request("GET", `/api/playlists/${id}`),

  getPublicAll: () => request("GET", "/api/playlists/public"),

  getPublicById: (id) => request("GET", `/api/playlists/public/${id}`),

  create: (name, description, coverUrl) =>
    request("POST", "/api/playlists", { name, description, coverUrl }),

  update: (id, name, description, coverUrl) =>
    request("PUT", `/api/playlists/${id}`, { name, description, coverUrl }),

  remove: (id) => request("DELETE", `/api/playlists/${id}`),

  addSong: (playlistId, songId) => 
    request("POST", `/api/playlists/${playlistId}/songs/${songId}`),

  removeSong: (playlistId, songId) => 
    request("DELETE", `/api/playlists/${playlistId}/songs/${songId}`),
};
