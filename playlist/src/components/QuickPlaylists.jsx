import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { usePlaylists } from "../contexts/PlaylistContext";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMusic } from "@fortawesome/free-solid-svg-icons";

// Playlists curadas: capas são imagens estáticas (sem depender de IDs do banco)
const curatedPlaylists = [
  {
    id: "rock-classics",
    title: "Rock Clássico",
    color: "hsl(0deg 70% 40%)",
    covers: [
      "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/95/fd/b9/95fdb9b2-6d2b-92a6-97f2-51c1a6d77f1a/00602527874609.rgb.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/aa/e0/ab/aae0ab6a-d906-a189-81bf-70b56aa43f7a/886445635843.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/8b/f1/50/8bf1503a-a4db-4fa6-a3a5-f919509acacd/14UMGIM43392.rgb.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/69/9c/b5/699cb5d6-115c-ff73-9d26-e57ea4350d72/887828031795.png/300x300bb.jpg",
    ],
  },
  {
    id: "chill-vibes",
    title: "Chill Vibes",
    color: "hsl(210deg 70% 40%)",
    covers: [
      "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/b9/b4/2a/b9b42ad1-1e25-5096-da43-497a247e69a3/190295978051.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/07/60/ba/0760ba0f-148c-b18f-d0ff-169ee96f3af5/634904078164.png/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/e2/e4/9b/e2e49bdf-c92c-2ff9-c7bd-7e651f2aa6b3/886444642743.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/bd/df/9d/bddf9d26-d45a-278f-26c1-e5274094cb27/190295671600.jpg/300x300bb.jpg",
    ],
  },
  {
    id: "beatles-essentials",
    title: "Beatles Essentials",
    color: "hsl(45deg 90% 40%)",
    covers: [
      "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/d1/82/d4/d182d41a-bcbc-fbec-0e67-402efc414b04/26UMGIM82692.rgb.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/df/db/61/dfdb615d-47f8-06e9-9533-b96daccc029f/18UMGIM31076.rgb.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/1a/19/db/1a19db26-17ad-b986-11a9-f72ac7a6194b/18UMGIM31214.rgb.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/ae/98/4c/ae984c7a-cd06-a7cd-e8bf-32cb15ba698d/00602567705475.rgb.jpg/300x300bb.jpg",
    ],
  },
  {
    id: "grunge-era",
    title: "Era Grunge",
    color: "hsl(270deg 50% 40%)",
    covers: [
      "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/95/fd/b9/95fdb9b2-6d2b-92a6-97f2-51c1a6d77f1a/00602527874609.rgb.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/e3/20/03/e32003a4-99bc-1c70-40ba-001882f35dba/00602537526840.rgb.jpg/300x300bb.jpg",
    ],
  },
  {
    id: "classic-rock",
    title: "Clássicos do Rock",
    color: "hsl(141deg 50% 30%)",
    covers: [
      "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/8b/f1/50/8bf1503a-a4db-4fa6-a3a5-f919509acacd/14UMGIM43392.rgb.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/aa/e0/ab/aae0ab6a-d906-a189-81bf-70b56aa43f7a/886445635843.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/b9/b4/2a/b9b42ad1-1e25-5096-da43-497a247e69a3/190295978051.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music114/v4/5f/fa/56/5ffa56c2-ea1f-7a17-6bad-192ff9b6476d/825646124206.jpg/300x300bb.jpg",
    ],
  },
  {
    id: "brit-rock",
    title: "Brit Rock",
    color: "hsl(190deg 70% 35%)",
    covers: [
      "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/69/9c/b5/699cb5d6-115c-ff73-9d26-e57ea4350d72/887828031795.png/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/1f/46/84/1f468438-c8ff-6c7c-d790-7d9ad31a55b6/dj.ofwxjvjm.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/e2/e4/9b/e2e49bdf-c92c-2ff9-c7bd-7e651f2aa6b3/886444642743.jpg/300x300bb.jpg",
      "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/b9/b4/2a/b9b42ad1-1e25-5096-da43-497a247e69a3/190295978051.jpg/300x300bb.jpg",
    ],
  },
];

// ── Card for curated playlists (link to /songs) ──────────────────────────────
const CuratedCard = ({ pl }) => (
  <Link to="/songs" className="playlist-card" style={{ "--pl-color": pl.color }}>
    <div className="playlist-card__mosaic">
      {pl.covers.map((src, i) => (
        <img key={i} src={src} alt="" className="playlist-card__mosaic-img" />
      ))}
    </div>
    <span className="playlist-card__name">{pl.title}</span>
  </Link>
);

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

  const showUserPlaylists = user && playlists.length > 0;
  const items = showUserPlaylists ? playlists.slice(0, 6) : curatedPlaylists;
  const title = showUserPlaylists ? "Suas playlists" : "Playlists populares";

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
