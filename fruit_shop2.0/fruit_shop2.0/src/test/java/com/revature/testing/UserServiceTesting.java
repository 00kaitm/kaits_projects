package com.revature.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.dtos.UserDTO;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.User;
import com.revature.models.UserRole;
import com.revature.repositories.UserRepository;
import com.revature.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTesting {

	static UserRepository userRepo; 
	static UserService userService; 
	static UserRole userR; 
	static List<User> users = new ArrayList<>(); 	
	static User u1; 
	static User u2; 
	static User u3; 
	
	
	@BeforeAll
	public static void set() {
		userRepo = mock(UserRepository.class); 
		userService = new UserService(userRepo); 
		u1 = new User(1, "kaitm", "123", userR);
	}
	
	@Test 
	public void getAllUserTest() {
		when(userRepo.findAll()).thenReturn(users);
		assertEquals(users, userService.getAll());
	}
	
	@Test 
	public void getUserByIdTest() throws UserNotFoundException {
		when(userRepo.findById(1)).thenReturn(Optional.of(u1)); 
		assertEquals(new UserDTO(u1), userService.getUserById(1)); 
	}
	
	@Test
	public void createNewUserTest() {
		when(userRepo.findUserByUsername("kaitm")).thenReturn(u1); 
		assertEquals(u1, userService.createUser(u1)); 
	}
	
	@Test
	public void updateUserTest() throws UserNotFoundException{
		when(userRepo.findUserByUsername("kaitm")).thenReturn(u1);
		when(userRepo.save(u1)).thenReturn(u1); 
		assertEquals(u1, userService.updateUser(1, u1));
	}
	
	@Test void deleteUserTest() throws UserNotFoundException {
		when(userRepo.findById(1)).thenReturn(Optional.of(u1));
		assertEquals(true, userService.deleteUser(1));
	}
}
