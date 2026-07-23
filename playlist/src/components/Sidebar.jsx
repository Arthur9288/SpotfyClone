import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faHouse,
  faMagnifyingGlass,
  faPlus,
  faMusic,
  faChevronRight,
  faList,
} from "@fortawesome/free-solid-svg-icons";
import { useAuth } from "../contexts/AuthContext";
import { usePlaylists } from "../contexts/PlaylistContext";

const Sidebar = ({ onCreatePlaylist }) => {
  const { user }           = useAuth();
  const { playlists }      = usePlaylists();
  const navigate           = useNavigate();
  const [filter, setFilter] = useState("all"); // "all" | "playlists"

  // Capa da playlist: primeira música do array songs[] retornado pela API
  const getPlaylistCover = (pl) => {
    return pl.songs?.[0]?.imageUrl ?? null;
  };

  return (
    <aside className="sidebar">
      {/* ── Navigation ─────────────────────────────────────────────── */}
      <nav className="sidebar__nav">
        <Link to="/" className="sidebar__nav-link" id="sidebar-home">
          <FontAwesomeIcon icon={faHouse} className="sidebar__nav-icon" />
          <span>Início</span>
        </Link>
        <Link to="/songs" className="sidebar__nav-link" id="sidebar-search">
          <FontAwesomeIcon icon={faMagnifyingGlass} className="sidebar__nav-icon" />
          <span>Buscar</span>
        </Link>
      </nav>

      {/* ── Library ────────────────────────────────────────────────── */}
      <div className="sidebar__library">
        {/* Library header */}
        <div className="sidebar__library-header">
          <button className="sidebar__library-title-btn">
            <FontAwesomeIcon icon={faList} />
            <span>Sua Biblioteca</span>
          </button>

          {user && (
            <button
              className="sidebar__create-btn"
              onClick={onCreatePlaylist}
              title="Criar playlist"
              id="btn-create-playlist"
              aria-label="Criar nova playlist"
            >
              <FontAwesomeIcon icon={faPlus} />
            </button>
          )}
        </div>

        {/* Filter pills */}
        {user && playlists.length > 0 && (
          <div className="sidebar__filters">
            <button
              className={`sidebar__filter-pill${filter === "all" ? " sidebar__filter-pill--active" : ""}`}
              onClick={() => setFilter("all")}
            >
              Tudo
            </button>
            <button
              className={`sidebar__filter-pill${filter === "playlists" ? " sidebar__filter-pill--active" : ""}`}
              onClick={() => setFilter("playlists")}
            >
              Playlists
            </button>
          </div>
        )}

        {/* Content */}
        <div className="sidebar__library-content">
          {!user ? (
            /* Not logged in — prompt */
            <div className="sidebar__promo">
              <div className="sidebar__promo-card">
                <p className="sidebar__promo-title">Crie sua primeira playlist</p>
                <p className="sidebar__promo-text">É fácil, vamos te ajudar.</p>
                <Link to="/register">
                  <button className="sidebar__promo-btn">Criar playlist</button>
                </Link>
              </div>
            </div>
          ) : playlists.length === 0 ? (
            /* Logged in but no playlists */
            <div className="sidebar__promo">
              <div className="sidebar__promo-card">
                <p className="sidebar__promo-title">Crie sua primeira playlist</p>
                <p className="sidebar__promo-text">É fácil, vamos te ajudar.</p>
                <button className="sidebar__promo-btn" onClick={onCreatePlaylist}>
                  Criar playlist
                </button>
              </div>
            </div>
          ) : (
            /* Playlist list */
            <ul className="sidebar__playlist-list">
              {playlists.map((pl) => {
                const cover = getPlaylistCover(pl);
                return (
                  <li key={pl.id}>
                    <Link
                      to={`/playlist/${pl.id}`}
                      className="sidebar__playlist-item"
                    >
                      <div className="sidebar__playlist-cover">
                        {cover ? (
                          <img src={cover} alt={pl.name} />
                        ) : (
                          <div className="sidebar__playlist-cover--empty">
                            <FontAwesomeIcon icon={faMusic} />
                          </div>
                        )}
                      </div>
                      <div className="sidebar__playlist-info">
                        <p className="sidebar__playlist-name">{pl.name}</p>
                        <p className="sidebar__playlist-meta">
                          Playlist • {pl.songs?.length ?? 0} música{(pl.songs?.length ?? 0) !== 1 ? "s" : ""}
                        </p>
                      </div>
                    </Link>
                  </li>
                );
              })}
            </ul>
          )}
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
