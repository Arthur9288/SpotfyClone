import React, { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCirclePlay,
  faPen,
  faTrash,
  faMusic,
  faArrowLeft,
  faClock,
} from "@fortawesome/free-solid-svg-icons";
import { usePlaylists } from "../contexts/PlaylistContext";
import CreatePlaylistModal from "../components/CreatePlaylistModal";

import { playlistsApi } from "../services/api";

const Playlist = () => {
  const { id }       = useParams();
  const navigate     = useNavigate();
  const { getPlaylist, deletePlaylist, removeSong } = usePlaylists();
  
  const [editOpen, setEditOpen] = useState(false);
  const [publicPlaylist, setPublicPlaylist] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);

  // Se a playlist pertence ao usuário, ela estará no context.
  const contextPlaylist = getPlaylist(id);

  useEffect(() => {
    // Se achou no context, não precisa buscar na API pública
    if (contextPlaylist) return;

    const fetchPublic = async () => {
      setLoading(true);
      try {
        const data = await playlistsApi.getPublicById(id);
        setPublicPlaylist(data);
      } catch (e) {
        setError(true);
      } finally {
        setLoading(false);
      }
    };
    fetchPublic();
  }, [id, contextPlaylist]);

  const playlist = contextPlaylist || publicPlaylist;
  const isOwner = !!contextPlaylist; // Só deixa editar/deletar se for dono

  if (loading) {
    return <div className="playlist-page"><p className="main__status">Carregando...</p></div>;
  }

  if (error || !playlist) {
    return (
      <div className="playlist-not-found">
        <p>Playlist não encontrada.</p>
        <Link to="/">Voltar para o início</Link>
      </div>
    );
  }

  // O backend retorna o objeto playlist com songs[] completo
  const songs = playlist.songs ?? [];

  const handleDelete = () => {
    if (window.confirm(`Deletar a playlist "${playlist.name}"?`)) {
      deletePlaylist(id);
      navigate("/");
    }
  };

  const totalDuration = songs.reduce((acc, s) => {
    const [m, sec] = (s.duration ?? "0:00").split(":").map(Number);
    return acc + m * 60 + (sec || 0);
  }, 0);

  const formatTotal = (secs) => {
    const h = Math.floor(secs / 3600);
    const m = Math.floor((secs % 3600) / 60);
    if (h > 0) return `${h} h ${m} min`;
    return `${m} min`;
  };

  return (
    <>
      <div className="playlist-page">
        {/* Hero header */}
        <div className="playlist-page__hero">
          {/* Cover mosaic or empty state */}
          <div className="playlist-page__cover">
            {songs.length > 0 ? (
              <div className="playlist-page__mosaic">
                {songs.slice(0, 4).map((s, i) => (
                  <img key={i} src={s.imageUrl} alt={s.name} />
                ))}
              </div>
            ) : (
              <div className="playlist-page__cover--empty">
                <FontAwesomeIcon icon={faMusic} />
              </div>
            )}
          </div>

          {/* Info */}
          <div className="playlist-page__info">
            <p className="playlist-page__type">Playlist</p>
            <h1 className="playlist-page__title">{playlist.name}</h1>
            <p className="playlist-page__meta">
              {songs.length} música{songs.length !== 1 ? "s" : ""}
              {songs.length > 0 && ` • ${formatTotal(totalDuration)}`}
            </p>
          </div>
        </div>

        {/* Actions bar */}
        <div className="playlist-page__actions">
        {songs.length > 0 && (() => {
            const queueStr = songs.map((s) => s.id).join(",");
            const params = new URLSearchParams({ queue: queueStr, pos: 0, playlist: id });
            return (
              <Link to={`/song/${songs[0].id}?${params.toString()}`}>
                <FontAwesomeIcon
                  icon={faCirclePlay}
                  className="playlist-page__play-btn"
                  title="Reproduzir"
                />
              </Link>
            );
          })()}

          {isOwner && (
            <>
              <button
                className="playlist-page__action-btn"
                onClick={() => setEditOpen(true)}
                title="Editar playlist"
                id="btn-edit-playlist"
              >
                <FontAwesomeIcon icon={faPen} />
                Editar
              </button>

              <button
                className="playlist-page__action-btn playlist-page__action-btn--danger"
                onClick={handleDelete}
                title="Deletar playlist"
                id="btn-delete-playlist"
              >
                <FontAwesomeIcon icon={faTrash} />
                Deletar
              </button>
            </>
          )}
        </div>

        {/* Song table */}
        {songs.length === 0 ? (
          <div className="playlist-page__empty">
            <FontAwesomeIcon icon={faMusic} className="playlist-page__empty-icon" />
            <p>Esta playlist ainda não tem músicas.</p>
            {isOwner && (
              <button className="playlist-page__add-btn" onClick={() => setEditOpen(true)}>
                Adicionar músicas
              </button>
            )}
          </div>
        ) : (
          <table className="playlist-table">
            <thead>
              <tr className="playlist-table__head">
                <th className="playlist-table__th playlist-table__th--num">#</th>
                <th className="playlist-table__th">Título</th>
                <th className="playlist-table__th playlist-table__th--artist">Artista</th>
                <th className="playlist-table__th playlist-table__th--dur">
                  <FontAwesomeIcon icon={faClock} />
                </th>
                <th className="playlist-table__th" />
              </tr>
            </thead>
            <tbody>
              {songs.map((song, i) => {
                const queueStr = songs.map((s) => s.id).join(",");
                const params = new URLSearchParams({ queue: queueStr, pos: i, playlist: id });
                const songUrl = `/song/${song.id}?${params.toString()}`;

                return (
                  <tr key={song.id} className="playlist-table__row">
                    <td className="playlist-table__td playlist-table__td--num">{i + 1}</td>
                    <td className="playlist-table__td">
                      <div className="playlist-table__song">
                        <img src={song.imageUrl} alt={song.name} className="playlist-table__img" />
                        <Link to={songUrl} className="playlist-table__name">
                          {song.name}
                        </Link>
                      </div>
                    </td>
                    <td className="playlist-table__td playlist-table__td--artist">
                      <Link to={`/artist/${song.artistId}`} className="playlist-table__artist">
                        {song.artistName}
                      </Link>
                    </td>
                    <td className="playlist-table__td playlist-table__td--dur">{song.duration}</td>
                    <td className="playlist-table__td">
                      {isOwner && (
                        <button
                          className="playlist-table__remove"
                          onClick={() => removeSong(id, song.id)}
                          title="Remover da playlist"
                          aria-label={`Remover ${song.name}`}
                        >
                          ✕
                        </button>
                      )}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>

      {/* Edit modal */}
      {editOpen && (
        <CreatePlaylistModal
          editPlaylist={playlist}
          onClose={() => setEditOpen(false)}
        />
      )}
    </>
  );
};

export default Playlist;
