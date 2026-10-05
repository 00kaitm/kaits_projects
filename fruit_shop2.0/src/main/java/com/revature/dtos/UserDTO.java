package com.revature.dtos;

import java.util.Objects;

import com.revature.models.User;

public class UserDTO {

	private int id;
	private String username;
	private String role;

	public UserDTO() {
	}

	public UserDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.role = user.getRole() == null ? null : user.getRole().name();
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }

	@Override
	public int hashCode() {
		return Objects.hash(id, username, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		UserDTO other = (UserDTO) obj;
		return id == other.id && Objects.equals(username, other.username) && Objects.equals(role, other.role);
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", username=" + username + ", role=" + role + "]";
	}
}