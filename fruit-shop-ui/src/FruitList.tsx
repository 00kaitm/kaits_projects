import { useEffect, useState } from "react";
import { api } from "./api";
import type { Fruit, Session } from "./api";

export default function FruitList({ session }: { session: Session }) {
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
        <section>
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
        </section>
    );
}