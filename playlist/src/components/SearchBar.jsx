import React, { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass, faXmark } from "@fortawesome/free-solid-svg-icons";
import { songsApi, artistsApi } from "../services/api";

const DEBOUNCE_MS = 300;

const SearchBar = () => {
  const [query, setQuery]     = useState("");
  const [results, setResults] = useState([]);
  const [focused, setFocused] = useState(false);
  const [loading, setLoading] = useState(false);
  const navigate              = useNavigate();
  const debounceRef           = useRef(null);

  // Busca com debounce: espera o usuário parar de digitar antes de chamar a API
  useEffect(() => {
    if (debounceRef.current) clearTimeout(debounceRef.current);

    if (!query.trim()) {
      setResults([]);
      return;
    }

    debounceRef.current = setTimeout(async () => {
      setLoading(true);
      try {
        const [songs, artists] = await Promise.all([
          songsApi.search(query),
          artistsApi.getAll(),
        ]);

        const q = query.toLowerCase();

        const matchedSongs = (songs ?? [])
          .slice(0, 5)
          .map((s) => ({
            id:     s.id,
            name:   s.name,
            image:  s.imageUrl,
            artist: s.artistName,
            type:   "song",
          }));

        const matchedArtists = (artists ?? [])
          .filter((a) => a.name.toLowerCase().includes(q))
          .slice(0, 3)
          .map((a) => ({
            id:    a.id,
            name:  a.name,
            image: a.imageUrl,
            type:  "artist",
          }));

        setResults([...matchedArtists, ...matchedSongs]);
      } catch (err) {
        console.error("Erro na busca:", err);
        setResults([]);
      } finally {
        setLoading(false);
      }
    }, DEBOUNCE_MS);

    return () => clearTimeout(debounceRef.current);
  }, [query]);

  const handleSelect = (item) => {
    setQuery("");
    setResults([]);
    if (item.type === "artist") navigate(`/artist/${item.id}`);
    else navigate(`/song/${item.id}`);
  };

  const handleClear = () => {
    setQuery("");
    setResults([]);
  };

  return (
    <div className={`search-bar${focused ? " search-bar--focused" : ""}`}>
      <div className="search-bar__input-wrap">
        <FontAwesomeIcon className="search-bar__icon" icon={faMagnifyingGlass} />
        <input
          className="search-bar__input"
          type="text"
          placeholder="O que você quer ouvir?"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onFocus={() => setFocused(true)}
          onBlur={() => setTimeout(() => setFocused(false), 150)}
          aria-label="Buscar músicas e artistas"
          id="search-input"
        />
        {query && (
          <button className="search-bar__clear" onClick={handleClear} aria-label="Limpar busca">
            <FontAwesomeIcon icon={faXmark} />
          </button>
        )}
      </div>

      {focused && loading && (
        <div className="search-bar__dropdown search-bar__dropdown--empty">
          <p>Buscando...</p>
        </div>
      )}

      {focused && !loading && results.length > 0 && (
        <div className="search-bar__dropdown">
          {results.map((item) => (
            <button
              key={`${item.type}-${item.id}`}
              className="search-bar__result"
              onMouseDown={() => handleSelect(item)}
            >
              <img
                src={item.image}
                alt={item.name}
                className={`search-bar__result-img${item.type === "artist" ? " search-bar__result-img--round" : ""}`}
              />
              <div className="search-bar__result-info">
                <p className="search-bar__result-name">{item.name}</p>
                <p className="search-bar__result-type">
                  {item.type === "artist" ? "Artista" : item.artist}
                </p>
              </div>
            </button>
          ))}
        </div>
      )}

      {focused && !loading && query && results.length === 0 && (
        <div className="search-bar__dropdown search-bar__dropdown--empty">
          <p>Nenhum resultado para "<strong>{query}</strong>"</p>
        </div>
      )}
    </div>
  );
};

export default SearchBar;
