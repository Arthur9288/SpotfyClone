import React from "react";
import ItemList from "./ItemList";
import useApi from "../hooks/useApi";
import { songsApi, artistsApi, playlistsApi } from "../services/api";

// Componente de loading/erro reutilizável
const ApiState = ({ loading, error, children }) => {
  if (loading) return <p className="main__status">Carregando...</p>;
  if (error)   return <p className="main__status main__status--error">Erro ao carregar dados.</p>;
  return children;
};

const Main = ({ type }) => {
  const showArtists = type === "artists" || type === undefined;
  const showSongs   = type === "songs"   || type === undefined;

  const {
    data: artistsData,
    loading: artistsLoading,
    error: artistsError,
  } = useApi(() => artistsApi.getAll(), []);

  const {
    data: songsData,
    loading: songsLoading,
    error: songsError,
  } = useApi(() => songsApi.getAll(), []);

  // A API retorna { id, name, imageUrl, bannerUrl } — adapta para o contrato do SingleItem
  const artists = (artistsData ?? []).map((a) => ({
    id:     a.id,
    name:   a.name,
    image:  a.imageUrl,
    banner: a.bannerUrl,
  }));

  // A API retorna { id, name, imageUrl, artistName } — adapta para o contrato do SingleItem
  const songs = (songsData ?? []).map((s) => ({
    id:     s.id,
    name:   s.name,
    image:  s.imageUrl,
    artist: s.artistName,
  }));

  const {
    data: playlistsData,
    loading: playlistsLoading,
    error: playlistsError,
  } = useApi(() => playlistsApi.getPublicAll(), []);

  // Adapta para o contrato do SingleItem, usando mosaico se disponível
  const playlists = (playlistsData ?? []).map((p) => {
    // Pega a imagem das 4 primeiras músicas
    const covers = (p.songs ?? []).slice(0, 4).map(s => s.imageUrl);

    return {
      id:     p.id,
      name:   p.name,
      // Passa o array de covers para criar o mosaico, se não tiver passa null
      covers: covers.length > 0 ? covers : null,
      image:  p.coverUrl, // fallback
      artist: "Curadoria Spotify", // Subtítulo
    };
  });

  // Mostra as 6 playlists na mesma linha (ou quantas a API retornar)
  const playlistsCuradas = playlists;

  return (
    <div className="main">
      {/* ── Playlists Curadas no topo (Abaixo de Minhas Playlists) ── */}
      {showArtists && (
        <ApiState loading={playlistsLoading} error={playlistsError}>
          <ItemList
            title="Playlists Populares"
            items={6}
            itemsArray={playlistsCuradas}
            path={null} /* path={null} esconde o "Mostrar Tudo" */
            idPath="/playlist"
          />
        </ApiState>
      )}

      {/* ── Artistas e Músicas ── */}
      {showArtists && (
        <ApiState loading={artistsLoading} error={artistsError}>
          <ItemList
            title="Artistas Populares"
            items={10}
            itemsArray={artists}
            path="/artists"
            idPath="/artist"
          />
        </ApiState>
      )}

      {showSongs && (
        <ApiState loading={songsLoading} error={songsError}>
          <ItemList
            title="Músicas Populares"
            items={15}
            itemsArray={songs}
            path="/songs"
            idPath="/song"
          />
        </ApiState>
      )}
    </div>
  );
};

export default Main;
