import React, { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext";
import { PlaylistProvider } from "./contexts/PlaylistContext";
import ProtectedRoute from "./components/ProtectedRoute";
import Header from "./components/Header";
import Sidebar from "./components/Sidebar";
import CreatePlaylistModal from "./components/CreatePlaylistModal";

import Home     from "./pages/Home";
import Artists  from "./pages/Artists";
import Artist   from "./pages/Artist";
import Songs    from "./pages/Songs";
import Song     from "./pages/Song";
import Playlist from "./pages/Playlist";
import Login    from "./pages/Login";
import Register from "./pages/Register";

// ── Inner layout (needs router context for Sidebar links) ────────────────────
const AppLayout = () => {
  const [modalOpen, setModalOpen] = useState(false);

  return (
    <>
      <Header />

      <div className="app-body">
        <Sidebar onCreatePlaylist={() => setModalOpen(true)} />

        <main className="app-content">
          <Routes>
            <Route path="/"               element={<Home />} />
            <Route path="/artists"        element={<Artists />} />
            <Route path="/artist/:id"     element={<Artist />} />
            <Route path="/songs"          element={<Songs />} />
            <Route path="/song/:id"       element={<Song />} />
            <Route
              path="/playlist/:id"
              element={
                <ProtectedRoute>
                  <Playlist />
                </ProtectedRoute>
              }
            />
          </Routes>
        </main>
      </div>

      {modalOpen && (
        <CreatePlaylistModal onClose={() => setModalOpen(false)} />
      )}
    </>
  );
};

// ── Root ─────────────────────────────────────────────────────────────────────
const App = () => (
  <AuthProvider>
    <PlaylistProvider>
      <BrowserRouter>
        <Routes>
          {/* Public auth pages (no layout) */}
          <Route path="/login"    element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* App with sidebar layout */}
          <Route path="/*" element={<AppLayout />} />
        </Routes>
      </BrowserRouter>
    </PlaylistProvider>
  </AuthProvider>
);

export default App;
