import { useEffect, useState } from "react";
import { api } from "./api";
import type { Fruit, Session } from "./api";

const EMOJI: Record<string, string> = {
    apple: "🍎", strawberry: "🍓", mango: "🥭", lime: "🍋", lemon: "🍋",
    watermelon: "🍉", orange: "🍊", kiwi: "🥝", banana: "🍌", grape: "🍇",
    pineapple: "🍍", peach: "🍑", cherry: "🍒", pear: "🍐", blueberr: "🫐",
    coconut: "🥥", melon: "🍈", tomato: "🍅", avocado: "🥑",
};

function emojiFor(name: string) {
    const lower = name.toLowerCase();
    const match = Object.keys(EMOJI).find((key) => lower.includes(key));
    return match ? EMOJI[match] : "🧺";
}

export default function FruitList({ session }: { session: Session }) {
    const isAdmin = session.role === "ADMIN";
    const [fruits, setFruits] = useState<Fruit[]>([]);
    const [error, setError] = useState("");
    const [version, setVersion] = useState(0);
    const [mineOnly, setMineOnly] = useState(false);

    const [editingId, setEditingId] = useState<number | null>(null);
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [price, setPrice] = useState("");

    useEffect(() => {
        api<Fruit[]>(mineOnly ? "/fruit/mine" : "/fruit", { token: session.token })
            .then(setFruits)
            .catch((err: Error) => setError(err.message));
    }, [session.token, version, mineOnly]);

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
        window.scrollTo({ top: 0, behavior: "smooth" });
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
        <section>
            {error && <p role="alert">{error}</p>}

            {isAdmin && (
                <form
                    className="panel"
                    onSubmit={(event) => {
                        event.preventDefault();
                        void save();
                    }}
                >
                    <h2>{editingId === null ? "Add a fruit" : "Edit fruit"}</h2>
                    <div className="row">
                        <input
                            placeholder="Name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                        <input
                            placeholder="Description"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                        />
                        <input
                            placeholder="Price"
                            type="number"
                            step="0.01"
                            value={price}
                            onChange={(e) => setPrice(e.target.value)}
                            required
                        />
                        <button type="submit">{editingId === null ? "Add" : "Save"}</button>
                        {editingId !== null && (
                            <button type="button" onClick={resetForm}>
                                Cancel
                            </button>
                        )}
                    </div>
                </form>
            )}

            <div className="row heading">
                <h2>{mineOnly ? "My fruits" : "All fruits"}</h2>
                <button onClick={() => setMineOnly(!mineOnly)}>
                    {mineOnly ? "Show all fruits" : "Show my fruits"}
                </button>
            </div>
            {fruits.length === 0 && <p className="muted">No fruits here yet.</p>}

            <ul className="grid">
                {fruits.map((fruit) => (
                    <li key={fruit.id} className="card">
            <span className="emoji" aria-hidden="true">
              {emojiFor(fruit.name)}
            </span>
                        <h3>{fruit.name}</h3>
                        <p className="muted">{fruit.description}</p>
                        <p className="price">${fruit.price.toFixed(2)}</p>
                        <p className="muted small">Sold by {fruit.hidden?.username ?? "the shop"}</p>
                        {isAdmin && (
                            <div className="row">
                                <button onClick={() => startEdit(fruit)}>Edit</button>
                                <button className="danger" onClick={() => void remove(fruit)}>
                                    Delete
                                </button>
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        </section>
    );
}