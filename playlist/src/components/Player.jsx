import React, { useRef, useState, useEffect, useCallback } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCirclePlay,
  faCirclePause,
  faBackwardStep,
  faForwardStep,
  faVolumeHigh,
  faVolumeLow,
  faVolumeXmark,
} from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";

const BASE_URL = import.meta.env.VITE_API_URL ?? "";

const Player = ({ audio, duration, prevId, nextId, queueParam, posParam, playlistId }) => {
  // Se a URL de áudio for relativa (/api/...), prefixamos com a URL do backend
  const audioSrc = audio?.startsWith("/api") ? `${BASE_URL}${audio}` : audio;
  const audioRef        = useRef(null);
  const [isPlaying, setIsPlaying]         = useState(false);
  const [currentTime, setCurrentTime]     = useState(0);
  const [totalDuration, setTotalDuration] = useState(0);
  // Lê o volume salvo; padrão 1 se nunca foi alterado
  const [volume, setVolume]     = useState(() => parseFloat(localStorage.getItem("player_volume") ?? "1"));
  const [prevVolume, setPrevVolume] = useState(() => parseFloat(localStorage.getItem("player_volume") ?? "1"));
  const navigate = useNavigate();

  // Sanitiza playlistId — evita strings "null" ou "undefined" vindas da URL
  const validPlaylistId =
    playlistId && playlistId !== "null" && playlistId !== "undefined"
      ? playlistId
      : null;

  // Monta URL de prev/next preservando a fila completa
  const buildUrl = useCallback((targetId, offset) => {
    if (!targetId) return null;
    if (queueParam && posParam !== null) {
      const newPos = Number(posParam) + offset;
      const params = new URLSearchParams({ queue: queueParam, pos: newPos });
      if (validPlaylistId) params.set("playlist", validPlaylistId);
      return `/song/${targetId}?${params.toString()}`;
    }
    return `/song/${targetId}`;
  }, [queueParam, posParam, validPlaylistId]);

  const prevUrl = buildUrl(prevId, -1);
  const nextUrl = buildUrl(nextId, +1);

  // Quando o src do áudio muda: reseta estados e tenta reproduzir
  // Aguarda o evento 'canplay' antes de chamar play() para evitar AbortError
  useEffect(() => {
    const el = audioRef.current;
    if (!el || !audioSrc) return;

    setCurrentTime(0);
    setTotalDuration(0);
    setIsPlaying(false);

    const handleCanPlay = () => {
      el.play()
        .then(() => setIsPlaying(true))
        .catch(() => setIsPlaying(false));
    };

    el.addEventListener("canplay", handleCanPlay, { once: true });
    el.load();
    el.volume = volume;

    return () => {
      el.removeEventListener("canplay", handleCanPlay);
    };
  }, [audioSrc]);

  const togglePlay = () => {
    const el = audioRef.current;
    if (!el) return;
    if (isPlaying) {
      el.pause();
      setIsPlaying(false);
    } else {
      el.play()
        .then(() => setIsPlaying(true))
        .catch(() => setIsPlaying(false));
    }
  };

  const handleTimeUpdate  = () => setCurrentTime(audioRef.current.currentTime);
  const handleLoadedMetadata = () => setTotalDuration(audioRef.current.duration);

  const handleSeek = (e) => {
    const bar    = e.currentTarget;
    const clickX = e.nativeEvent.offsetX;
    const width  = bar.offsetWidth;
    const newTime = (clickX / width) * (totalDuration || 1);
    audioRef.current.currentTime = newTime;
    setCurrentTime(newTime);
  };

  const handleEnded = () => {
    setIsPlaying(false);
    if (nextUrl) navigate(nextUrl);
  };

  const formatTime = (secs) => {
    const m = Math.floor(secs / 60);
    const s = Math.floor(secs % 60).toString().padStart(2, "0");
    return `${m}:${s}`;
  };

  const progressPercent = totalDuration ? (currentTime / totalDuration) * 100 : 0;

  const handleVolumeChange = (e) => {
    const val = parseFloat(e.target.value);
    setVolume(val);
    if (audioRef.current) audioRef.current.volume = val;
    if (val > 0) setPrevVolume(val);
    localStorage.setItem("player_volume", val); // persiste
  };

  const toggleMute = () => {
    if (volume > 0) {
      setPrevVolume(volume);
      setVolume(0);
      if (audioRef.current) audioRef.current.volume = 0;
      // Não salva 0 como volume preferido — restaura ao desmutar
    } else {
      setVolume(prevVolume);
      if (audioRef.current) audioRef.current.volume = prevVolume;
      localStorage.setItem("player_volume", prevVolume);
    }
  };

  const volumeIcon = volume === 0 ? faVolumeXmark : volume < 0.5 ? faVolumeLow : faVolumeHigh;

  return (
    <div className="player">
      {/*
        key={audio} força o React a destruir e recriar o elemento <audio>
        sempre que o src mudar — garante que não há src residual do áudio anterior
      */}
      <audio
        key={audioSrc}
        ref={audioRef}
        src={audioSrc}
        onTimeUpdate={handleTimeUpdate}
        onLoadedMetadata={handleLoadedMetadata}
        onEnded={handleEnded}
      />

      <div className="player__controllers">
        <FontAwesomeIcon
          className="player__icon"
          icon={faBackwardStep}
          onClick={() => { if (prevUrl) navigate(prevUrl); }}
          style={{ cursor: prevUrl ? "pointer" : "default", opacity: prevUrl ? 1 : 0.4 }}
        />

        <FontAwesomeIcon
          className="player__icon player__icon--play"
          icon={isPlaying ? faCirclePause : faCirclePlay}
          onClick={togglePlay}
          style={{ cursor: "pointer" }}
        />

        <FontAwesomeIcon
          className="player__icon"
          icon={faForwardStep}
          onClick={() => { if (nextUrl) navigate(nextUrl); }}
          style={{ cursor: nextUrl ? "pointer" : "default", opacity: nextUrl ? 1 : 0.4 }}
        />
      </div>

      <div className="player__progress">
        <p>{formatTime(currentTime)}</p>

        <div
          className="player__bar"
          onClick={handleSeek}
          style={{ cursor: "pointer" }}
        >
          <div
            className="player__bar-progress"
            style={{ width: `${progressPercent}%` }}
          />
        </div>

        <p>{totalDuration ? formatTime(totalDuration) : "--:--"}</p>
      </div>

      {/* Volume */}
      <div className="player__volume">
        <FontAwesomeIcon
          className="player__icon player__icon--volume"
          icon={volumeIcon}
          onClick={toggleMute}
          style={{ cursor: "pointer" }}
        />
        <input
          id="volume-bar"
          className="player__volume-bar"
          type="range"
          min="0"
          max="1"
          step="0.02"
          value={volume}
          onChange={handleVolumeChange}
          style={{
            background: `linear-gradient(to right, var(--text-primary) ${volume * 100}%, hsl(0deg 0% 100% / 20%) ${volume * 100}%)`
          }}
        />
      </div>
    </div>
  );
};

export default Player;
