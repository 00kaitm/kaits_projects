package com.revature.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.revature.dtos.UserDTO;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

	private final UserRepository ur;
	private final PasswordEncoder encoder;


	public UserService(UserRepository ur, PasswordEncoder encoder) {
		this.ur = ur;
		this.encoder = encoder;
	} 
	
	public UserDTO getUserById(int id) throws UserNotFoundException{
		User user = ur.findById(id).orElseThrow(UserNotFoundException::new);
		return new UserDTO(user);
	}

	@Transactional
	public User createUser(User newUser) {
		newUser.setPassword(encoder.encode(newUser.getPassword()));
		return ur.save(newUser);
	}
	
	public List<User> getAll(){
		return ur.findAll();
	}

	@Transactional
	public User updateUser(int id, User user) throws UserNotFoundException {
		User existing = ur.findById(id).orElseThrow(UserNotFoundException::new);
		existing.setUsername(user.getUsername());
		existing.setPassword(encoder.encode(user.getPassword()));
		return ur.save(existing);
	}
	
	@Transactional
	public boolean deleteUser(int id) throws UserNotFoundException{
		getUserById(id); 
		ur.deleteById(id);
		return true;
	}
}
