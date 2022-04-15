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

		
		private UserService us;
		private AuthService authServ; 
		
		private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

		
		@Autowired
		public UserController(UserService us, AuthService au) {
			super();
			this.us = us;
			this.authServ = au; 
		} 
		
		@GetMapping
		public ResponseEntity<List<UserDTO>> getAll(@RequestHeader(value = "Authorization", required = false) String token) throws AuthException, BadTokenException{
			MDC.put("requestId", UUID.randomUUID().toString());
			if(!authServ.verify(token, UserRole.ADMIN, UserRole.BASIC_USER)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			List<User> users = us.getAll();
			List<UserDTO> protectedUsers = new ArrayList<>();
			for(User user : users) { 
				protectedUsers.add(new UserDTO(user)); 
				}
			LOG.info("all users retrieved.");
			return new ResponseEntity<>(protectedUsers, HttpStatus.OK);
		} 
		
		@GetMapping("/{id}")
		public ResponseEntity<UserDTO> getById(@PathVariable("id") int id, @RequestHeader("Authorization") String token) throws UserNotFoundException, AuthException, BadTokenException {
			MDC.put("requestId", UUID.randomUUID().toString());
			if (!authServ.verify(token)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			LOG.info("user by id: " + id + "retrieved.");
			return new ResponseEntity<>(us.getUserById(id), HttpStatus.OK);
		}
		
		@PostMapping
		public ResponseEntity<String> createUser(@RequestBody User user) {
			User u = us.createUser(user);
			MDC.put("User has been created: ", user.getId());
			LOG.info("new user by id: "+ u.getId() + " created." );
			return new ResponseEntity<>("User " + u.getUsername() + " has been created.", HttpStatus.CREATED);
		}

		@PutMapping("/{id}")
		public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("id") int id) {
			LOG.info("user by id: " + user.getId() + " has been updated. ");
			MDC.put("User updated by id: ", user.getId());
			return new ResponseEntity<>(us.updateUser(id, user), HttpStatus.CREATED);
		}

		@DeleteMapping("/{id}")
		public ResponseEntity<String> DeleteById(@PathVariable("id") int id) throws UserNotFoundException {
			us.deleteUser(id);
			LOG.info("user by id: " + id + " has been deleted.");
			MDC.put("User has been deleted: ", id);
			return new ResponseEntity<>("User was deleted", HttpStatus.OK);
		}
		
		
		
}
