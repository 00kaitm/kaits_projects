package com.revature.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(FruitNotFoundException.class)
	public ResponseEntity<String> handleFruitNotFoundException(FruitNotFoundException e){
 		LOG.warn("Fruit not found exception was handled.", e);
		return new ResponseEntity<>("Fruit doesn't exist", HttpStatus.NOT_FOUND); 
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e){
		LOG.warn("User not found exception was handled.", e);
		return new ResponseEntity<>("User doesn't exist", HttpStatus.NOT_FOUND); 
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidation(MethodArgumentNotValidException e) {
		String message = e.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + " " + err.getDefaultMessage())
				.collect(Collectors.joining(", "));
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(AuthException.class)
	public ResponseEntity<String> handleAuthException(AuthException e) {
		LOG.warn("Authentication exception was handled.", e);
		return new ResponseEntity<>("Not Authorized", HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(BadTokenException.class)
	public ResponseEntity<String> handleBadTokenException(BadTokenException e) {
		LOG.warn("Bad Token exception was handled.", e);
		return new ResponseEntity<>("Not Authorized", HttpStatus.UNAUTHORIZED);
	}
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleConflict(DataIntegrityViolationException e) {
		LOG.warn("Data conflict was handled.", e);
		return new ResponseEntity<>("Conflicts with existing data: a duplicate name, or an item still in use", HttpStatus.CONFLICT);
	}
}
