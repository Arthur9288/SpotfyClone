import React, { useState, useEffect } from "react";
import Player from "../components/Player";
import { Link, useParams, useSearchParams } from "react-router-dom";
import { songsApi } from "../services/api";

const Song = () => {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const [songData, setSongData] = useState(null);
  const [loading, setLoading]   = useState(true);

  // Busca dados da música pela API (usa o DB ID real — sem mismatch)
  useEffect(() => {
    setLoading(true);
    setSongData(null);
    songsApi
      .getById(id)
      .then(setSongData)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [id]);

  // ── Modo Playlist: usa fila completa de IDs + posição atual ──────────────
  const queueParam = searchParams.get("queue");   // "1,5,10,3"
  const posParam   = searchParams.get("pos");     // "0"
  const playlistId = searchParams.get("playlist");

  let prevId = null;
  let nextId = null;

  if (queueParam && posParam !== null) {
    const queue = queueParam.split(",").map(Number);
    const pos   = Number(posParam);
    prevId = pos > 0               ? queue[pos - 1] : null;
    nextId = pos < queue.length - 1 ? queue[pos + 1] : null;
  }

  if (loading) {
    return (
      <div className="song">
        <div className="song__bar" style={{ justifyContent: "center" }}>
          <p style={{ color: "var(--text-secondary, #aaa)" }}>Carregando...</p>
        </div>
      </div>
    );
  }

  if (!songData) {
    return (
      <div className="song">
        <div className="song__bar" style={{ justifyContent: "center" }}>
          <p style={{ color: "var(--text-secondary, #aaa)" }}>Música não encontrada.</p>
        </div>
      </div>
    );
  }

  // A API retorna: { id, name, duration, imageUrl, audioUrl, artistName, artistId }
  // Usa artistId diretamente — sem depender de arrays locais
  return (
    <div className="song">
      <div className="song__container">
        <div className="song__image-container">
          <img src={songData.imageUrl} alt={`Imagem da música ${songData.name}`} />
        </div>
      </div>

      <div className="song__bar">
        {songData.artistId && (
          <Link to={`/artist/${songData.artistId}`} className="song__artist-image">
            <img
              width={75}
              height={75}
              src={songData.imageUrl}
              alt={`Imagem do Artista ${songData.artistName}`}
            />
          </Link>
        )}

        <Player
          audio={songData.audioUrl}
          duration={songData.duration}
          prevId={prevId}
          nextId={nextId}
          queueParam={queueParam}
          posParam={posParam}
          playlistId={playlistId}
        />

        <div className="song__info">
          <p className="song__name">{songData.name}</p>
          <p className="song__artist-name">{songData.artistName}</p>
        </div>
      </div>
    </div>
  );
};

export default Song;
