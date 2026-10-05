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
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

	private final UserRepository ur;
	private final PasswordEncoder encoder;
	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);


	public AuthService(UserRepository ur, PasswordEncoder encoder) {
		this.ur = ur;
		this.encoder = encoder;
	}
	public String login(String username, String password) throws AuthException {		User user = ur.findUserByUsername(username);
		if (user == null || !encoder.matches(password, user.getPassword())) {
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

//		User principal = ur.findById(Integer.valueOf(splitToken[0])).orElse(null);
		int userId;
		try {
			userId = Integer.parseInt(splitToken[0]);
		} catch (NumberFormatException e) {
			throw new BadTokenException();
		}
		User principal = ur.findById(userId).orElse(null);

		if(principal == null || !principal.getRole().toString().equals(splitToken[1]) || !Arrays.asList(roles).contains(principal.getRole())) {
			throw new AuthException();
		} 
		LOG.info("token verified successfully");
		MDC.put("userId", principal.getId());
		return true; 
	}
	
}
