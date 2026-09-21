package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.RegisterResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.type.AuthProviderType;

import jakarta.transaction.Transactional;



@Service
public class UserService {
	
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private   AuthenticationManager authenticationManager;
	
	
	
	@Autowired
private 	JwtService jwtService;
	
	
	
	public User signUp(LoginRequest loginRequest , AuthProviderType authProviderType, String providerId)
	{
		 User user = repo.findByUserName(loginRequest.getUsername()).orElse(null);

		    if (user != null)
		    {
		        throw new IllegalArgumentException("User already exist");
		    }
		    
		user = User.builder()
				.userName(loginRequest.getUsername())
				.providerId(providerId)
				.authProviderType(authProviderType)
			.build();
				

		   if(authProviderType == AuthProviderType.EMAIL)
		   {
			   user.setPassword(encoder.encode(loginRequest.getPassword()));
		   }
		    

		   
		    return repo.save(user);

		
	}
	
	
	public RegisterResponse register(LoginRequest loginRequest)
	{
		
		User newUser = signUp(loginRequest ,AuthProviderType.EMAIL, null);
	   
	    return new RegisterResponse(newUser.getId(), newUser.getUsername());
	}
	
	public LoginResponse login(LoginRequest request)
	{
	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

	    String username = authentication.getName();
	    User user = repo.findByUserName(username)
	            .orElseThrow(() -> new RuntimeException("User not found: " + username));

	    String token = jwtService.generateAccessToken(user);

	    return new LoginResponse(token, user.getId());
	}
	
	
@Transactional
public ResponseEntity<LoginResponse> handleOAuth2LoginRequest(OAuth2User auth2user, String registrationId) {
		
		
		AuthProviderType providerType = jwtService.getProviderTypeFromRegistrationId(registrationId);
		String providerId = jwtService.determineProviderIdFromAuthUser(auth2user, registrationId);
			User user  = repo.findByProviderIdAndAuthProviderType(providerId , providerType).orElse(null);
			
			
		
		String email = auth2user.getAttribute("email");
		
		User emailUser = repo.findByUserName(email).orElse(null); 
		 
		if(user == null &&  emailUser == null)
		{
			
			
			String username = jwtService.determineUsernameFromOAuth2User(auth2user, registrationId, providerId);
		
			user =		signUp(new LoginRequest(username , null), providerType, providerId);
			
		}
		else if (user != null)
		{
			if(email != null && !email.isBlank() && !email.equals(user.getUsername()))
			{
				user.setUsername(email);
				repo.save(user);
			}
		}
			else
			{
				throw new BadCredentialsException("this email is already register" + emailUser.getAuthProviderType());
			}
		
		LoginResponse loginResponse = new LoginResponse(jwtService.generateAccessToken(user), user.getId());
		
		return ResponseEntity.ok(loginResponse);
		
		// fetch provider type and provider id also save it
		
		
		
		// if the user has an accout so directly login
		
		//otherwise signup and than login
		
		
		
		
	
	
	
	

	}
}
