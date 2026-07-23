import React, { createContext, useContext, useState, useEffect } from "react";
import { authApi } from "../services/api";

import { signInWithPopup } from "firebase/auth";
import { auth, googleProvider } from "../services/firebase";

// ─── Context ────────────────────────────────────────────────────────────────
const AuthContext = createContext(null);

// ─── Hook ───────────────────────────────────────────────────────────────────
export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside <AuthProvider>");
  return ctx;
};

// ─── Provider ───────────────────────────────────────────────────────────────
export const AuthProvider = ({ children }) => {
  const [user, setUser]       = useState(null);  // { id, name, email, avatar }
  const [loading, setLoading] = useState(true);  // hydrating from localStorage

  // Hydrate from localStorage on mount
  useEffect(() => {
    try {
      const stored = localStorage.getItem("spotify_user");
      const token  = localStorage.getItem("spotify_token");
      if (stored && token) setUser(JSON.parse(stored));
    } catch (_) {}
    finally { setLoading(false); }
  }, []);

  // ── helpers ──────────────────────────────────────────────────────────────
  const persist = (userData, token) => {
    localStorage.setItem("spotify_user",  JSON.stringify(userData));
    localStorage.setItem("spotify_token", token);
    setUser(userData);
  };

  const clear = () => {
    localStorage.removeItem("spotify_user");
    localStorage.removeItem("spotify_token");
    setUser(null);
  };

  // ── Auth actions ─────────────────────────────────────────────────────────

  const register = async (name, email, password) => {
    const res = await authApi.register(name, email, password);
    // res = { token: "...", name: "...", email: "..." }
    const userObj = { name: res.name, email: res.email };
    persist(userObj, res.token);
  };

  const login = async (email, password) => {
    const res = await authApi.login(email, password);
    const userObj = { name: res.name, email: res.email };
    persist(userObj, res.token);
  };

  const loginWithGoogle = async () => {
    try {
      const result = await signInWithPopup(auth, googleProvider);
      const token = await result.user.getIdToken();
      
      // Envia o token do Google para o nosso Spring Boot validar e devolver o JWT dele
      const res = await authApi.loginWithGoogle(token);
      
      const userObj = { name: res.name, email: res.email };
      persist(userObj, res.token);
    } catch (err) {
      console.error(err);
      throw new Error("Falha ao entrar com Google.");
    }
  };

  /** Logout */
  const logout = () => clear();

  // ─── Value ────────────────────────────────────────────────────────────────
  return (
    <AuthContext.Provider value={{ user, loading, login, loginWithGoogle, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// ─── Util ────────────────────────────────────────────────────────────────────
const fakeDelay = (ms = 800) => new Promise((r) => setTimeout(r, ms));
