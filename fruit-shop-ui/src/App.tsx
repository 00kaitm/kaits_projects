import { useState } from "react";
import { SESSION_KEY } from "./api";
import type { Session } from "./api";
import Login from "./Login";
import FruitList from "./FruitList";
import UsersPage from "./UsersPage";

function loadSession(): Session | null {
  try {
    const saved = localStorage.getItem(SESSION_KEY);
    return saved ? (JSON.parse(saved) as Session) : null;
  } catch {
    return null;
  }
}

export default function App() {
  const [session, setSession] = useState<Session | null>(loadSession);
  const [page, setPage] = useState<"fruits" | "users">("fruits");

  function login(newSession: Session) {
    localStorage.setItem(SESSION_KEY, JSON.stringify(newSession));
    setPage("fruits");
    setSession(newSession);
  }

  function logout() {
    localStorage.removeItem(SESSION_KEY);
    setSession(null);
  }

  if (!session) {
    return <Login onLogin={login} />;
  }

  const isAdmin = session.role === "ADMIN";

  return (
      <>
        <header className="topbar">
          <span className="brand">🍊 Fruit Shop</span>
          {isAdmin && (
              <nav>
                <button disabled={page === "fruits"} onClick={() => setPage("fruits")}>
                  Fruits
                </button>
                <button disabled={page === "users"} onClick={() => setPage("users")}>
                  Users
                </button>
              </nav>
          )}
          <span className="who">
          {session.username} · {isAdmin ? "Admin" : "Member"}
            <button onClick={logout}>Log out</button>
        </span>
        </header>
        <main>
          {isAdmin && page === "users" ? (
              <UsersPage session={session} />
          ) : (
              <FruitList session={session} />
          )}
        </main>
      </>
  );
}