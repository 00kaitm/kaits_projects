import { useEffect, useState } from "react";
import { api } from "./api";
import type { Session, User } from "./api";

export default function UsersPage({ session }: { session: Session }) {
    const [users, setUsers] = useState<User[]>([]);
    const [error, setError] = useState("");
    const [version, setVersion] = useState(0);

    useEffect(() => {
        api<User[]>("/users", { token: session.token })
            .then(setUsers)
            .catch((err: Error) => setError(err.message));
    }, [session.token, version]);

    async function run(action: () => Promise<unknown>) {
        setError("");
        try {
            await action();
            setVersion((v) => v + 1);
        } catch (err) {
            setError(err instanceof Error ? err.message : "Something went wrong.");
        }
    }

    function toggleRole(user: User) {
        const role = user.role === "ADMIN" ? "BASIC_USER" : "ADMIN";
        return run(() =>
            api(`/users/${user.id}/role?role=${role}`, { method: "PUT", token: session.token }),
        );
    }

    function remove(user: User) {
        if (!window.confirm(`Delete ${user.username}?`)) return;

        return run(() => api(`/users/${user.id}`, { method: "DELETE", token: session.token }));
    }

    return (
        <section>
            <h2>Users</h2>
            {error && <p role="alert">{error}</p>}
            <table>
                <thead>
                <tr>
                    <th>Username</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {users.map((user) => {
                    const isMe = user.id === session.id;
                    return (
                        <tr key={user.id}>
                            <td>{user.username}{isMe && " (you)"}</td>
                            <td>{user.role}</td>
                            <td>
                                <button disabled={isMe} onClick={() => void toggleRole(user)}>
                                    {user.role === "ADMIN" ? "Make basic user" : "Make admin"}
                                </button>{" "}
                                <button className="danger" disabled={isMe} onClick={() => void remove(user)}>
                                    Delete
                                </button>
                            </td>
                        </tr>
                    );
                })}
                </tbody>
            </table>
        </section>
    );
}
