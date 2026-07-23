import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import logoSpotify from "../assets/logo/spotify-logo.png";

const Login = () => {
  const { login, loginWithGoogle } = useAuth();
  const navigate = useNavigate();

  const [email,    setEmail]    = useState("");
  const [password, setPassword] = useState("");
  const [error,    setError]    = useState("");
  const [loading,  setLoading]  = useState(false);
  const [googleLoading, setGoogleLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await login(email, password);
      navigate("/");
    } catch (err) {
      setError(err.message || "Erro ao entrar. Tente novamente.");
    } finally {
      setLoading(false);
    }
  };

  const handleGoogle = async () => {
    setError("");
    setGoogleLoading(true);
    try {
      await loginWithGoogle();
      navigate("/");
    } catch (err) {
      setError("Erro ao entrar com Google.");
    } finally {
      setGoogleLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        {/* Logo */}
        <div className="auth-card__logo">
          <img src={logoSpotify} alt="Spotify" />
          <span>Spotify</span>
        </div>

        <h1 className="auth-card__title">Entrar no Spotify</h1>

        {/* Google button */}
        <button
          className="auth-btn auth-btn--google"
          onClick={handleGoogle}
          disabled={googleLoading || loading}
          id="btn-login-google"
        >
          <svg className="auth-btn__google-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
            <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
            <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/>
            <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/>
          </svg>
          {googleLoading ? "Entrando..." : "Continuar com Google"}
        </button>

        {/* Divider */}
        <div className="auth-divider">
          <span />
          <p>ou</p>
          <span />
        </div>

        {/* Email/password form */}
        <form className="auth-form" onSubmit={handleSubmit} noValidate>
          <div className="auth-form__group">
            <label className="auth-form__label" htmlFor="login-email">
              E-mail
            </label>
            <input
              id="login-email"
              className="auth-form__input"
              type="email"
              placeholder="nome@email.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              autoComplete="email"
            />
          </div>

          <div className="auth-form__group">
            <label className="auth-form__label" htmlFor="login-password">
              Senha
            </label>
            <input
              id="login-password"
              className="auth-form__input"
              type="password"
              placeholder="Sua senha"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              autoComplete="current-password"
            />
          </div>

          {error && <p className="auth-form__error" role="alert">{error}</p>}

          <button
            id="btn-login-submit"
            className="auth-btn auth-btn--primary"
            type="submit"
            disabled={loading || googleLoading}
          >
            {loading ? "Entrando..." : "Entrar"}
          </button>
        </form>

        <p className="auth-card__footer">
          Não tem uma conta?{" "}
          <Link to="/register" className="auth-card__link">
            Cadastre-se
          </Link>
        </p>
      </div>
    </div>
  );
};

export default Login;
