package com.revature.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.revature.dtos.UserDTO;
import com.revature.exceptions.AuthException;
import com.revature.exceptions.BadTokenException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.User;
import com.revature.models.UserRole;
import com.revature.service.AuthService;
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
		List<UserDTO> users = new ArrayList<>();
		for (User user : us.getAll()) {
			users.add(new UserDTO(user));
		}
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getById(@PathVariable("id") int id) throws UserNotFoundException {
		return ResponseEntity.ok(us.getUserById(id));
	}
		
		@PostMapping
		public ResponseEntity<String> createUser(@RequestBody User user) {
			User u = us.createUser(user);
			MDC.put("User has been created: ", user.getId());
			LOG.info("new user by id: "+ u.getId() + " created." );
			return new ResponseEntity<>("User " + u.getUsername() + " has been created.", HttpStatus.CREATED);
		}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("id") int id) throws UserNotFoundException {
		User updated = us.updateUser(id, user);
		LOG.info("user by id: " + id + " has been updated. ");
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}

		@DeleteMapping("/{id}")
		public ResponseEntity<String> DeleteById(@PathVariable("id") int id) throws UserNotFoundException {
			us.deleteUser(id);
			LOG.info("user by id: " + id + " has been deleted.");
			MDC.put("User has been deleted: ", id);
			return new ResponseEntity<>("User was deleted", HttpStatus.OK);
		}
		
		
		
}
