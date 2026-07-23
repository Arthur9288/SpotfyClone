import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCirclePlay } from "@fortawesome/free-solid-svg-icons";
import { Link, useParams } from "react-router-dom";
import SongList from "../components/SongList";
import useApi from "../hooks/useApi";
import { artistsApi } from "../services/api";

const Artist = () => {
  const { id } = useParams();

  const { data: artist, loading, error } = useApi(() => artistsApi.getById(id), [id]);

  if (loading) {
    return (
      <div className="artist">
        <p style={{ padding: "2rem", color: "var(--text-secondary, #aaa)" }}>
          Carregando artista...
        </p>
      </div>
    );
  }

  if (error || !artist) {
    return (
      <div className="artist">
        <p style={{ padding: "2rem", color: "var(--text-secondary, #aaa)" }}>
          Artista não encontrado.
        </p>
      </div>
    );
  }

  // A API retorna: { id, name, imageUrl, bannerUrl, songs: [...] }
  const songs = artist.songs ?? [];

  const randomIndex       = songs.length > 0 ? Math.floor(Math.random() * songs.length) : null;
  const randomIdFromArtist = randomIndex !== null ? songs[randomIndex].id : null;

  return (
    <div className="artist">
      <div
        className="artist__header"
        style={{
          backgroundImage: `linear-gradient(to bottom, var(--_shade), var(--_shade)),url(${artist.bannerUrl})`,
        }}
      >
        <h2 className="artist__title">{artist.name}</h2>
      </div>

      <div className="artist__body">
        <h2>Populares</h2>

        {/* SongList recebe o array de músicas normalizado */}
        <SongList
          songsArray={songs.map((s) => ({
            id:         s.id,
            name:       s.name,
            duration:   s.duration,
            image:      s.imageUrl,
            audio:      s.audioUrl,
            artist:     artist.name,
            artistId:   artist.id,
          }))}
        />
      </div>

      {randomIdFromArtist !== null && (
        <Link to={`/song/${randomIdFromArtist}`}>
          <FontAwesomeIcon
            className="single-item__icon single-item__icon--artist"
            icon={faCirclePlay}
          />
        </Link>
      )}
    </div>
  );
};

export default Artist;
