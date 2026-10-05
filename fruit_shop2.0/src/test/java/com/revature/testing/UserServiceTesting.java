package com.revature.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.revature.dtos.UserDTO;
import com.revature.dtos.UserRequest;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.User;
import com.revature.models.UserRole;
import com.revature.repositories.UserRepository;
import com.revature.service.UserService;

public class UserServiceTesting {

	private UserRepository userRepo;
	private PasswordEncoder encoder;
	private UserService userService;
	private User u1;

	@BeforeEach
	public void setUp() {
		userRepo = mock(UserRepository.class);
		encoder = new BCryptPasswordEncoder();
		userService = new UserService(userRepo, encoder);
		u1 = new User(1, "kaitm", "stored-hash", UserRole.BASIC_USER);
	}

	@Test
	public void createHashesPasswordAndForcesBasicRole() {
		when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

		userService.createUser(new UserRequest("newbie", "hunter2hunter2"));

		ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
		verify(userRepo).save(saved.capture());
		assertEquals(UserRole.BASIC_USER, saved.getValue().getRole());
		assertNotEquals("hunter2hunter2", saved.getValue().getPassword());
		assertTrue(encoder.matches("hunter2hunter2", saved.getValue().getPassword()));
	}

	@Test
	public void getByIdReturnsUser() {
		when(userRepo.findById(1)).thenReturn(Optional.of(u1));
		assertEquals(new UserDTO(u1), userService.getUserById(1));
	}

	@Test
	public void getByIdThrowsWhenMissing() {
		when(userRepo.findById(99)).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> userService.getUserById(99));
	}

	@Test
	public void updateChangesUsernameAndRehashesPassword() {
		when(userRepo.findById(1)).thenReturn(Optional.of(u1));

		UserDTO result = userService.updateUser(1, new UserRequest("kaitlyn", "newpassword1"));

		assertEquals("kaitlyn", result.getUsername());
		assertTrue(encoder.matches("newpassword1", u1.getPassword()));
		assertEquals(UserRole.BASIC_USER, u1.getRole());
	}

	@Test
	public void updateRoleChangesRole() {
		when(userRepo.findById(1)).thenReturn(Optional.of(u1));
		userService.updateRole(1, UserRole.ADMIN);
		assertEquals(UserRole.ADMIN, u1.getRole());
	}

	@Test
	public void deleteRemovesTheUser() {
		when(userRepo.findById(1)).thenReturn(Optional.of(u1));
		userService.deleteUser(1);
		verify(userRepo).delete(u1);
	}
}