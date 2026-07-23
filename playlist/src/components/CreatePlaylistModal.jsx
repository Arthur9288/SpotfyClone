import React, { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faXmark, faMagnifyingGlass, faCheck, faMusic } from "@fortawesome/free-solid-svg-icons";
import { usePlaylists } from "../contexts/PlaylistContext";
import { songsApi } from "../services/api";

const CreatePlaylistModal = ({ onClose, editPlaylist = null }) => {
  const { createPlaylist, updatePlaylist, addSong, removeSong } = usePlaylists();
  const navigate   = useNavigate();
  const inputRef   = useRef(null);
  const isEditing  = Boolean(editPlaylist);

  const [name,       setName]       = useState(editPlaylist?.name ?? "");
  const [search,     setSearch]     = useState("");
  const [dbSongs,    setDbSongs]    = useState([]);
  // No modo edição, pré-seleciona as músicas que já estão na playlist (usa songs[].id da API)
  const [selectedIds, setSelectedIds] = useState(
    new Set((editPlaylist?.songs ?? []).map((s) => s.id))
  );

  // Focus name input on mount
  useEffect(() => { inputRef.current?.focus(); }, []);

  // Busca as músicas da API
  useEffect(() => {
    songsApi.getAll().then(setDbSongs).catch(console.error);
  }, []);

  // Close on Escape
  useEffect(() => {
    const handler = (e) => { if (e.key === "Escape") onClose(); };
    window.addEventListener("keydown", handler);
    return () => window.removeEventListener("keydown", handler);
  }, [onClose]);

  const filteredSongs = dbSongs.filter(
    (s) =>
      s.name.toLowerCase().includes(search.toLowerCase()) ||
      (s.artistName && s.artistName.toLowerCase().includes(search.toLowerCase()))
  );

  const toggleSong = (id) => {
    setSelectedIds((prev) => {
      const next = new Set(prev);
      next.has(id) ? next.delete(id) : next.add(id);
      return next;
    });
  };

  const [saving, setSaving] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const finalName = name.trim() || "Minha playlist";
    const newSelectedIds = Array.from(selectedIds);

    setSaving(true);
    try {
      if (isEditing) {
        // 1. Atualiza nome
        await updatePlaylist(editPlaylist.id, { name: finalName });

        // 2. Calcula diff: quais adicionar e quais remover
        const originalIds = new Set((editPlaylist.songs ?? []).map((s) => s.id));
        const toAdd    = newSelectedIds.filter((id) => !originalIds.has(id));
        const toRemove = [...originalIds].filter((id) => !selectedIds.has(id));

        for (const songId of toAdd)    await addSong(editPlaylist.id, songId);
        for (const songId of toRemove) await removeSong(editPlaylist.id, songId);

        onClose();
      } else {
        // 1. Cria a playlist no banco
        const pl = await createPlaylist(finalName);

        // 2. Adiciona músicas selecionadas uma a uma
        for (const songId of newSelectedIds) {
          await addSong(pl.id, songId);
        }

        onClose();
        navigate(`/playlist/${pl.id}`);
      }
    } catch (err) {
      alert("Erro ao salvar playlist: " + err.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className="modal" role="dialog" aria-modal="true" aria-label={isEditing ? "Editar playlist" : "Criar playlist"}>
        {/* Header */}
        <div className="modal__header">
          <h2 className="modal__title">
            {isEditing ? "Editar playlist" : "Criar nova playlist"}
          </h2>
          <button className="modal__close" onClick={onClose} aria-label="Fechar">
            <FontAwesomeIcon icon={faXmark} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="modal__body">
          {/* Playlist name */}
          <div className="modal__field">
            <label className="modal__label" htmlFor="playlist-name">
              Nome da playlist
            </label>
            <input
              id="playlist-name"
              ref={inputRef}
              className="modal__input"
              type="text"
              placeholder="Minha playlist"
              value={name}
              onChange={(e) => setName(e.target.value)}
              maxLength={100}
            />
          </div>

          {/* Song search */}
          <div className="modal__field">
            <label className="modal__label">
              Adicionar músicas{" "}
              <span className="modal__label-count">
                ({selectedIds.size} selecionada{selectedIds.size !== 1 ? "s" : ""})
              </span>
            </label>
            <div className="modal__search">
              <FontAwesomeIcon icon={faMagnifyingGlass} className="modal__search-icon" />
              <input
                className="modal__search-input"
                type="text"
                placeholder="Buscar músicas ou artistas..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>
          </div>

          {/* Song list */}
          <div className="modal__song-list">
            {filteredSongs.slice(0, 50).map((song) => {
              const selected = selectedIds.has(song.id);
              return (
                <button
                  key={song.id}
                  type="button"
                  className={`modal__song-item${selected ? " modal__song-item--selected" : ""}`}
                  onClick={() => toggleSong(song.id)}
                >
                  <img
                    src={song.imageUrl}
                    alt={song.name}
                    className="modal__song-img"
                  />
                  <div className="modal__song-info">
                    <p className="modal__song-name">{song.name}</p>
                    <p className="modal__song-artist">{song.artistName}</p>
                  </div>
                  <div className={`modal__song-check${selected ? " modal__song-check--active" : ""}`}>
                    {selected && <FontAwesomeIcon icon={faCheck} />}
                  </div>
                </button>
              );
            })}
          </div>

          {/* Actions */}
          <div className="modal__actions">
            <button type="button" className="modal__btn modal__btn--ghost" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="modal__btn modal__btn--primary" id="btn-save-playlist" disabled={saving}>
              {saving ? "Salvando..." : isEditing ? "Salvar alterações" : "Criar playlist"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreatePlaylistModal;
