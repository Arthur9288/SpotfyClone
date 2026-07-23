import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

/**
 * Wraps any route that requires authentication.
 * If the user is not logged in, redirects to /login
 * and stores where they were trying to go (state.from)
 * so you can redirect back after login.
 */
const ProtectedRoute = ({ children }) => {
  const { user, loading } = useAuth();
  const location = useLocation();

  // While hydrating from localStorage, show nothing (avoids flash)
  if (loading) return null;

  if (!user) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return children;
};

export default ProtectedRoute;
