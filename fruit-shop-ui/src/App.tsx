import { useEffect, useState } from "react";
import { api } from "./api";
import type { Fruit, Session } from "./api";

export default function App() {
  const [session, setSession] = useState<Session | null>(null);

  if (!session) {
    return <Login onLogin={setSession} />;
  }
  return <FruitList session={session} onLogout={() => setSession(null)} />;
}

function Login({ onLogin }: { onLogin: (session: Session) => void }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  async function submit() {
    setError("");
    try {
      const session = await api<Session>("/auth", {
        method: "POST",
        body: { username, password },
      });
      onLogin(session);
    } catch {
      setError("Wrong username or password.");
    }
  }

  return (
      <main>
        <h1>Fruit Shop</h1>
        <form
            onSubmit={(event) => {
              event.preventDefault();
              void submit();
            }}
        >
          <p>
            <label>
              Username{" "}
              <input value={username} onChange={(e) => setUsername(e.target.value)} required />
            </label>
          </p>
          <p>
            <label>
              Password{" "}
              <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
              />
            </label>
          </p>
          <button type="submit">Log in</button>
        </form>
        {error && <p role="alert">{error}</p>}
      </main>
  );
}

function FruitList({ session, onLogout }: { session: Session; onLogout: () => void }) {
  const [fruits, setFruits] = useState<Fruit[]>([]);
  const [error, setError] = useState("");

  useEffect(() => {
    api<Fruit[]>("/fruit", { token: session.token })
        .then(setFruits)
        .catch((err: Error) => setError(err.message));
  }, [session.token]);

  return (
      <main>
        <h1>Fruit Shop</h1>
        <p>
          Logged in as {session.username} ({session.role}){" "}
          <button onClick={onLogout}>Log out</button>
        </p>
        {error && <p role="alert">{error}</p>}
        <table>
          <thead>
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Owner</th>
          </tr>
          </thead>
          <tbody>
          {fruits.map((fruit) => (
              <tr key={fruit.id}>
                <td>{fruit.name}</td>
                <td>{fruit.description}</td>
                <td>${fruit.price.toFixed(2)}</td>
                <td>{fruit.hidden?.username ?? "none"}</td>
              </tr>
          ))}
          </tbody>
        </table>
      </main>
  );
}