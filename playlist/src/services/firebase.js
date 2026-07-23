import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider } from "firebase/auth";

const firebaseConfig = {
  apiKey: "AIzaSyDzlIafnNaU-qLJGh1UTnAgpGSOzIbWFak",
  authDomain: "spotfy-clone-91c08.firebaseapp.com",
  projectId: "spotfy-clone-91c08",
  storageBucket: "spotfy-clone-91c08.firebasestorage.app",
  messagingSenderId: "822641969298",
  appId: "1:822641969298:web:41d5aea51ecd90b4123e4b"
};

// Inicializa o Firebase
const app = initializeApp(firebaseConfig);

// Inicializa o Auth e o Provedor do Google
export const auth = getAuth(app);
export const googleProvider = new GoogleAuthProvider();
