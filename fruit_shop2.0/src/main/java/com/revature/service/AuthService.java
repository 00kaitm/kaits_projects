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
	private final JwtService jwt;

	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);


	public AuthService(UserRepository ur, PasswordEncoder encoder, JwtService jwt) {
		this.ur = ur;
		this.encoder = encoder;
		this.jwt = jwt;
	}
	public String login(String username, String password) throws AuthException {		User user = ur.findUserByUsername(username);
		if (user == null || !encoder.matches(password, user.getPassword())) {
			throw new AuthException();
		}
		return jwt.createToken(user);
	}

	public boolean verify(String token, UserRole... roles) throws AuthException, BadTokenException {
		if (token == null) {
			throw new AuthException();
		}
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		int userId = jwt.getUserId(token);
		User principal = ur.findById(userId).orElse(null);
		if (principal == null || !Arrays.asList(roles).contains(principal.getRole())) {
			throw new AuthException();
		}
		MDC.put("userId", principal.getId());
		return true;
	}
}
