import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { usePlaylists } from "../contexts/PlaylistContext";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMusic } from "@fortawesome/free-solid-svg-icons";

import useApi from "../hooks/useApi";
import { playlistsApi } from "../services/api";

// ── Card for curated playlists (link to /playlist/:id) ───────────────────────
const CuratedCard = ({ pl }) => {
  const covers = (pl.songs ?? []).slice(0, 4);
  const accent = "hsl(210deg 70% 40%)";
  return (
    <Link to={`/playlist/${pl.id}`} className="playlist-card" style={{ "--pl-color": accent }}>
      <div className="playlist-card__mosaic">
        {covers.length > 0 ? (
          covers.map((song, i) => (
            <img key={i} src={song.imageUrl} alt="" className="playlist-card__mosaic-img" />
          ))
        ) : (
          <div className="playlist-card__mosaic-empty">
            <FontAwesomeIcon icon={faMusic} />
          </div>
        )}
      </div>
      <span className="playlist-card__name">{pl.name}</span>
    </Link>
  );
};

// ── Card for user playlists (dados vêm da API via PlaylistContext) ────────────
const UserCard = ({ pl }) => {
  // pl.songs já vem preenchido do banco via PlaylistContext
  const covers = (pl.songs ?? []).slice(0, 4);
  const accent = "hsl(141deg 50% 30%)";

  return (
    <Link to={`/playlist/${pl.id}`} className="playlist-card" style={{ "--pl-color": accent }}>
      <div className="playlist-card__mosaic">
        {covers.length > 0 ? (
          covers.map((song, i) => (
            <img key={i} src={song.imageUrl} alt={song.name} className="playlist-card__mosaic-img" />
          ))
        ) : (
          <div className="playlist-card__mosaic-empty">
            <FontAwesomeIcon icon={faMusic} />
          </div>
        )}
      </div>
      <span className="playlist-card__name">{pl.name}</span>
    </Link>
  );
};

// ── Component ────────────────────────────────────────────────────────────────
const QuickPlaylists = () => {
  const { user }      = useAuth();
  const { playlists } = usePlaylists();
  
  const { data: curatedData, loading } = useApi(() => playlistsApi.getPublicAll(), []);

  const showUserPlaylists = user && playlists.length > 0;
  const items = showUserPlaylists ? playlists.slice(0, 6) : (curatedData || []).slice(0, 6);
  const title = showUserPlaylists ? "Suas playlists" : "Playlists populares";

  if (loading && !showUserPlaylists) {
    return null;
  }

  return (
    <section className="quick-playlists">
      <h2 className="quick-playlists__title">{title}</h2>
      <div className="quick-playlists__grid">
        {showUserPlaylists
          ? items.map((pl) => <UserCard key={pl.id} pl={pl} />)
          : items.map((pl) => <CuratedCard key={pl.id} pl={pl} />)
        }
      </div>
    </section>
  );
};

export default QuickPlaylists;
