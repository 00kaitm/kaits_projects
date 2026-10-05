import { useState } from "react";
import { api } from "./api";
import type { Session } from "./api";

export default function Login({ onLogin }: { onLogin: (session: Session) => void }) {
    const [mode, setMode] = useState<"login" | "signup">("login");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    async function submit() {
        setError("");
        try {
            if (mode === "signup") {
                await api("/users", { method: "POST", body: { username, password } });
            }
            const session = await api<Session>("/auth", {
                method: "POST",
                body: { username, password },
            });
            onLogin(session);
        } catch (err) {
            const message = err instanceof Error ? err.message : "";
            if (mode === "login") {
                setError("Wrong username or password.");
            } else if (message.startsWith("Conflicts")) {
                setError("That username is already taken.");
            } else {
                setError(message || "Could not sign up.");
            }
        }
    }

    return (
        <main>
            <h1>Fruit Shop</h1>
            <h2>{mode === "login" ? "Log in" : "Create an account"}</h2>
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
                <button type="submit">{mode === "login" ? "Log in" : "Sign up"}</button>
            </form>
            {error && <p role="alert">{error}</p>}
            <p>
                {mode === "login" ? "New here? " : "Already have an account? "}
                <button
                    type="button"
                    onClick={() => {
                        setError("");
                        setMode(mode === "login" ? "signup" : "login");
                    }}
                >
                    {mode === "login" ? "Create an account" : "Log in"}
                </button>
            </p>
        </main>
    );
}