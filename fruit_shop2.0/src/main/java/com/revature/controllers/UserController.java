package com.revature.controllers;

import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.revature.dtos.UserDTO;
import com.revature.dtos.UserRequest;
import com.revature.models.UserRole;
import com.revature.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService us;

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	public UserController(UserService us) {
		this.us = us;
	}
	@GetMapping
	public ResponseEntity<List<UserDTO>> getAll() {
		return ResponseEntity.ok(us.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getById(@PathVariable("id") int id) {
		return ResponseEntity.ok(us.getUserById(id));
	}

	@PostMapping
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequest request) {
		UserDTO created = us.createUser(request);
		LOG.info("User {} created.", created.getId());
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserRequest request, @PathVariable("id") int id) {
		return ResponseEntity.ok(us.updateUser(id, request));
	}

	@PutMapping("/{id}/role")
	public ResponseEntity<UserDTO> updateRole(@PathVariable("id") int id, @RequestParam("role") UserRole role) {
		LOG.info("User {} role changed to {}.", id, role);
		return ResponseEntity.ok(us.updateRole(id, role));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") int id) {
		us.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}

