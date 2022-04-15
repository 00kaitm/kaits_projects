package com.revature.service;

import java.util.Arrays;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import com.revature.exceptions.AuthException;
import com.revature.exceptions.BadTokenException;
import com.revature.models.User;
import com.revature.models.UserRole;
import com.revature.repositories.UserRepository;

@Service
public class AuthService {

	private UserRepository ur;
	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

	
	@Autowired
	public AuthService(UserRepository ur) {
	 	super();
		this.ur = ur;
	} 
	public String login(@RequestParam("username") String username, @RequestParam("password") String password) throws AuthException{
		User user = ur.findUserByUsername(username);
	 	if(user == null || !user.getPassword().equals(password)) {
	 		throw new AuthException();
		}
	 	return user.getId()+":"+user.getRole().toString();	 
	}

	public boolean verify(String token, UserRole... roles) throws AuthException, BadTokenException {
		if(token == null) {
			LOG.info("Invalid token. ");
			throw new AuthException(); 
		}

		String[] splitToken = token.split(":");
		if(splitToken.length < 2) {
			throw new BadTokenException();
		}
		
		User principal = ur.findById(Integer.valueOf(splitToken[0])).orElse(null);
		if(principal == null || !principal.getRole().toString().equals(splitToken[1]) || !Arrays.asList(roles).contains(principal.getRole())) {
			throw new AuthException();
		} 
		LOG.info("token verified successfully");
		MDC.put("userId", principal.getId());
		return true; 
	}
	
}
