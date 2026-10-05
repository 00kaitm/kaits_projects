package com.revature.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.revature.dtos.UserDTO;
import com.revature.dtos.UserRequest;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.User;
import com.revature.models.UserRole;
import com.revature.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository ur;
	private final PasswordEncoder encoder;


	public UserService(UserRepository ur, PasswordEncoder encoder) {
		this.ur = ur;
		this.encoder = encoder;
	}

	@Transactional(readOnly = true)
	public List<UserDTO> getAll() {
		List<UserDTO> users = new ArrayList<>();
		for (User user : ur.findAll()) {
			users.add(new UserDTO(user));
		}
		return users;
	}

	@Transactional(readOnly = true)
	public UserDTO getUserById(int id) {
		return new UserDTO(findOrThrow(id));
	}

	@Transactional
	public UserDTO createUser(UserRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(encoder.encode(request.getPassword()));
		user.setRole(UserRole.BASIC_USER);
		return new UserDTO(ur.save(user));
	}

	@Transactional
	public UserDTO updateUser(int id, UserRequest request) {
		User existing = findOrThrow(id);
		existing.setUsername(request.getUsername());
		existing.setPassword(encoder.encode(request.getPassword()));
		return new UserDTO(existing);
	}

	@Transactional
	public UserDTO updateRole(int id, UserRole role) {
		User existing = findOrThrow(id);
		existing.setRole(role);
		return new UserDTO(existing);
	}

	@Transactional
	public void deleteUser(int id) {
		ur.delete(findOrThrow(id));
	}

	private User findOrThrow(int id) {
		return ur.findById(id).orElseThrow(UserNotFoundException::new);
	}

}
