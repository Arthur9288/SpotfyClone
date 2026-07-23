import React, { createContext, useContext, useState, useEffect } from "react";
import { useAuth } from "./AuthContext";
import { playlistsApi } from "../services/api";

const PlaylistContext = createContext(null);

export const usePlaylists = () => {
  const ctx = useContext(PlaylistContext);
  if (!ctx) throw new Error("usePlaylists must be used inside <PlaylistProvider>");
  return ctx;
};

export const PlaylistProvider = ({ children }) => {
  const { user, loading: authLoading } = useAuth();
  const [playlists, setPlaylists] = useState([]);

  // Carregar do Spring Boot quando logado
  const fetchPlaylists = async () => {
    if (!user) {
      setPlaylists([]);
      return;
    }
    try {
      const data = await playlistsApi.getAll();
      setPlaylists(data || []);
    } catch (err) {
      console.error("Failed to load playlists", err);
    }
  };

  useEffect(() => {
    // Só busca após o AuthContext terminar de hidratar do localStorage
    if (authLoading) return;
    fetchPlaylists();
  }, [user, authLoading]);

  // ── Actions (conectadas à API Real) ───────────────────────────────────────

  const createPlaylist = async (name, description = "", coverUrl = "") => {
    const newPlaylist = await playlistsApi.create(name, description, coverUrl);
    setPlaylists((prev) => [newPlaylist, ...prev]);
    return newPlaylist;
  };

  const updatePlaylist = async (id, { name, description, coverUrl }) => {
    const updated = await playlistsApi.update(id, name, description, coverUrl);
    setPlaylists((prev) => prev.map((pl) => (pl.id === id ? updated : pl)));
  };

  const deletePlaylist = async (id) => {
    await playlistsApi.remove(id);
    // eslint-disable-next-line eqeqeq
    setPlaylists((prev) => prev.filter((pl) => pl.id != id));
  };

  const addSong = async (playlistId, songId) => {
    const updated = await playlistsApi.addSong(playlistId, songId);
    // eslint-disable-next-line eqeqeq
    setPlaylists((prev) => prev.map((pl) => (pl.id == playlistId ? updated : pl)));
    return updated;
  };

  const removeSong = async (playlistId, songId) => {
    const updated = await playlistsApi.removeSong(playlistId, songId);
    // eslint-disable-next-line eqeqeq
    setPlaylists((prev) => prev.map((pl) => (pl.id == playlistId ? updated : pl)));
  };

  // id da URL vem como string, pl.id do banco vem como number — usa == para comparar
  // eslint-disable-next-line eqeqeq
  const getPlaylist = (id) => playlists.find((pl) => pl.id == id) ?? null;

  return (
    <PlaylistContext.Provider
      value={{ playlists, createPlaylist, updatePlaylist, deletePlaylist, addSong, removeSong, getPlaylist }}
    >
      {children}
    </PlaylistContext.Provider>
  );
};
