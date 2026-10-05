const API_URL = "http://localhost:8080";

export type Session = {
    token: string;
    id: number;
    username: string;
    role: "ADMIN" | "BASIC_USER";
};

export type Fruit = {
    id: number;
    name: string;
    description: string;
    price: number;
    hidden: { id: number; username: string } | null;
};

type Options = { method?: string; body?: unknown; token?: string };

export async function api<T>(path: string, options: Options = {}): Promise<T> {
    const headers: Record<string, string> = {};
    if (options.body !== undefined) headers["Content-Type"] = "application/json";
    if (options.token) headers["Authorization"] = `Bearer ${options.token}`;

    const response = await fetch(API_URL + path, {
        method: options.method ?? "GET",
        headers,
        body: options.body !== undefined ? JSON.stringify(options.body) : undefined,
    });

    const text = await response.text();
    const isJson = response.headers.get("content-type")?.includes("application/json");

    if (!response.ok) {
        if (response.status === 401) throw new Error("Please log in again.");
        if (response.status === 403) throw new Error("You don't have permission to do that.");
        throw new Error(isJson ? "Something went wrong." : text || "Something went wrong.");
    }
    return (isJson && text ? JSON.parse(text) : text) as T;
}