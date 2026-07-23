import React, { useState, useRef, useEffect } from "react";
import logoSpotify from "../assets/logo/spotify-logo.png";
import { Link, useLocation, useNavigate } from "react-router-dom";
import SearchBar from "./SearchBar";
import { useAuth } from "../contexts/AuthContext";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faChevronDown, faUser, faRightFromBracket } from "@fortawesome/free-solid-svg-icons";

const Header = () => {
  const { pathname } = useLocation();
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);
  const menuRef = useRef(null);

  const navLinks = [
    { to: "/",        label: "Início" },
    { to: "/artists", label: "Artistas" },
    { to: "/songs",   label: "Músicas" },
  ];

  // Close dropdown when clicking outside
  useEffect(() => {
    const handler = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) {
        setMenuOpen(false);
      }
    };
    document.addEventListener("mousedown", handler);
    return () => document.removeEventListener("mousedown", handler);
  }, []);

  const handleLogout = () => {
    logout();
    setMenuOpen(false);
    navigate("/login");
  };

  // First letter of name for avatar fallback
  const initials = user?.name?.charAt(0).toUpperCase() ?? "?";

  return (
    <header className="header">
      {/* Logo */}
      <Link to="/" className="header__logo-link">
        <img src={logoSpotify} alt="Logo do Spotify" />
        <h1>Spotify</h1>
      </Link>

      {/* Nav links */}
      <nav className="header__nav">
        {navLinks.map(({ to, label }) => (
          <Link
            key={to}
            to={to}
            className={`header__nav-link${pathname === to ? " header__nav-link--active" : ""}`}
          >
            {label}
          </Link>
        ))}
      </nav>

      {/* Search */}
      <SearchBar />

      {/* Auth section */}
      {user ? (
        /* ── Logged in: avatar + dropdown ── */
        <div className="header__user" ref={menuRef}>
          <button
            id="btn-user-menu"
            className="header__user-btn"
            onClick={() => setMenuOpen((o) => !o)}
            aria-expanded={menuOpen}
            aria-label="Menu do usuário"
          >
            {user.avatar ? (
              <img
                src={user.avatar}
                alt={user.name}
                className="header__user-avatar"
              />
            ) : (
              <div className="header__user-avatar header__user-avatar--initials">
                {initials}
              </div>
            )}
            <span className="header__user-name">{user.name}</span>
            <FontAwesomeIcon
              icon={faChevronDown}
              className={`header__user-chevron${menuOpen ? " header__user-chevron--open" : ""}`}
            />
          </button>

          {menuOpen && (
            <div className="header__user-menu">
              <div className="header__user-menu-info">
                <p className="header__user-menu-name">{user.name}</p>
                <p className="header__user-menu-email">{user.email}</p>
              </div>
              <hr className="header__user-menu-divider" />
              <button className="header__user-menu-item" disabled>
                <FontAwesomeIcon icon={faUser} />
                Perfil (em breve)
              </button>
              <button
                id="btn-logout"
                className="header__user-menu-item header__user-menu-item--danger"
                onClick={handleLogout}
              >
                <FontAwesomeIcon icon={faRightFromBracket} />
                Sair
              </button>
            </div>
          )}
        </div>
      ) : (
        /* ── Logged out: login / register buttons ── */
        <div className="header__auth">
          <Link to="/register">
            <button className="header__btn header__btn--ghost" id="btn-header-register">
              Cadastrar
            </button>
          </Link>
          <Link to="/login">
            <button className="header__btn header__btn--primary" id="btn-header-login">
              Entrar
            </button>
          </Link>
        </div>
      )}
    </header>
  );
};

export default Header;
