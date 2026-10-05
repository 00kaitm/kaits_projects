import { useState } from "react";
import type { Session } from "./api";
import Login from "./Login";
import FruitList from "./FruitList";
import UsersPage from "./UsersPage";

export default function App() {
  const [session, setSession] = useState<Session | null>(null);
  const [page, setPage] = useState<"fruits" | "users">("fruits");

  if (!session) {
    return (
        <Login
            onLogin={(newSession) => {
              setPage("fruits");
              setSession(newSession);
            }}
        />
    );
  }

  const isAdmin = session.role === "ADMIN";

  return (
      <main>
        <h1>Fruit Shop</h1>
        <p>
          Logged in as {session.username} ({session.role}){" "}
          <button onClick={() => setSession(null)}>Log out</button>
        </p>
        {isAdmin && (
            <nav>
              <button disabled={page === "fruits"} onClick={() => setPage("fruits")}>
                Fruits
              </button>{" "}
              <button disabled={page === "users"} onClick={() => setPage("users")}>
                Users
              </button>
            </nav>
        )}
        {isAdmin && page === "users" ? (
            <UsersPage session={session} />
        ) : (
            <FruitList session={session} />
        )}
      </main>
  );
}