package com.example.demo.error;

import java.nio.file.AccessDeniedException;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobleExceptionHandandler {
	
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiError> handleUsernameNotFoundException(UsernameNotFoundException ex)
	{
	ApiError apierror=	new ApiError("User not found with username"  + ex.getMessage(), HttpStatus.NOT_FOUND);
	
	return new ResponseEntity<>(apierror, apierror.getStatusCode());
		
		
	}

	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex)
	{
	ApiError apierror=	new ApiError("Authentication failed"  + ex.getMessage(), HttpStatus.UNAUTHORIZED);
	return new ResponseEntity<>(apierror, HttpStatus.UNAUTHORIZED);
		
		
	}
	

	@ExceptionHandler(JwtException.class)
	public ResponseEntity<ApiError> handlejwtException(JwtException ex)
	{
	ApiError apierror=	new ApiError("invalid Jwt token"  + ex.getMessage(), HttpStatus.NOT_FOUND);
	return new ResponseEntity<>(apierror, apierror.getStatusCode());
		
		
	}
	
	

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex)
	{
	ApiError apierror=	new ApiError("Access Denied insufficient permission"  + ex.getMessage(), HttpStatus.FORBIDDEN);
	return new ResponseEntity<>(apierror, HttpStatus.FORBIDDEN);
		
		
	}
	
	
	

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGenericExceptionException(Exception ex)
	{
	ApiError apierror=	new ApiError("An unexcepted error occured"  + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	return new ResponseEntity<>(apierror, HttpStatus.INTERNAL_SERVER_ERROR);
		
		
	}
	


}
