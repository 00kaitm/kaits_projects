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
  const isAdmin = session.role === "ADMIN";

  const [fruits, setFruits] = useState<Fruit[]>([]);
  const [error, setError] = useState("");
  const [version, setVersion] = useState(0);

  const [editingId, setEditingId] = useState<number | null>(null);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState("");

  useEffect(() => {
    api<Fruit[]>("/fruit", { token: session.token })
        .then(setFruits)
        .catch((err: Error) => setError(err.message));
  }, [session.token, version]);

  function resetForm() {
    setEditingId(null);
    setName("");
    setDescription("");
    setPrice("");
  }

  function startEdit(fruit: Fruit) {
    setEditingId(fruit.id);
    setName(fruit.name);
    setDescription(fruit.description ?? "");
    setPrice(String(fruit.price));
  }

  async function save() {
    setError("");
    try {
      await api<Fruit>(editingId === null ? "/fruit" : `/fruit/${editingId}`, {
        method: editingId === null ? "POST" : "PUT",
        token: session.token,
        body: { name, description, price: Number(price) },
      });
      resetForm();
      setVersion((v) => v + 1);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Something went wrong.");
    }
  }

  async function remove(fruit: Fruit) {
    if (!window.confirm(`Delete ${fruit.name}?`)) return;
    setError("");
    try {
      await api<string>(`/fruit/${fruit.id}`, { method: "DELETE", token: session.token });
      setVersion((v) => v + 1);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Something went wrong.");
    }
  }

  return (
      <main>
        <h1>Fruit Shop</h1>
        <p>
          Logged in as {session.username} ({session.role}){" "}
          <button onClick={onLogout}>Log out</button>
        </p>

        {error && <p role="alert">{error}</p>}

        {isAdmin && (
            <form
                onSubmit={(event) => {
                  event.preventDefault();
                  void save();
                }}
            >
              <h2>{editingId === null ? "Add a fruit" : "Edit fruit"}</h2>
              <input
                  placeholder="Name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
              />{" "}
              <input
                  placeholder="Description"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
              />{" "}
              <input
                  placeholder="Price"
                  type="number"
                  step="0.01"
                  value={price}
                  onChange={(e) => setPrice(e.target.value)}
                  required
              />{" "}
              <button type="submit">{editingId === null ? "Add" : "Save"}</button>{" "}
              {editingId !== null && (
                  <button type="button" onClick={resetForm}>
                    Cancel
                  </button>
              )}
            </form>
        )}

        <table>
          <thead>
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Owner</th>
            {isAdmin && <th>Actions</th>}
          </tr>
          </thead>
          <tbody>
          {fruits.map((fruit) => (
              <tr key={fruit.id}>
                <td>{fruit.name}</td>
                <td>{fruit.description}</td>
                <td>${fruit.price.toFixed(2)}</td>
                <td>{fruit.hidden?.username ?? "none"}</td>
                {isAdmin && (
                    <td>
                      <button onClick={() => startEdit(fruit)}>Edit</button>{" "}
                      <button onClick={() => void remove(fruit)}>Delete</button>
                    </td>
                )}
              </tr>
          ))}
          </tbody>
        </table>
      </main>
  );
}
