package com.revature.controllers;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.revature.dtos.LoginRequest;
import com.revature.dtos.LoginResponse;
import com.revature.exceptions.AuthException;
import com.revature.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService as;

	public AuthController(AuthService as) {
		this.as = as;
	}

	@PostMapping
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) throws AuthException {
		return ResponseEntity.ok(as.login(request.getUsername(), request.getPassword()));
	}
}