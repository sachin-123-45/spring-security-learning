package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.example.demo.entity.User;
import com.example.demo.type.AuthProviderType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtService {


	@Value("${jwt.secretKey}")
	private String jwtSecretKey;
	
	private SecretKey getSecretKey()
	{
		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
	}
	
	
	
	public String generateAccessToken(User user)
	{
		
		return Jwts.builder()
				.subject(user.getUsername())
				.claim("userId" , user.getId().toString())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000*60*10))
				.signWith(getSecretKey())
				
				.compact();
	}



	public String getUsernameFromToken(String token) {
	
		
		
		 Claims claims =  Jwts.parser()
				.verifyWith(getSecretKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
				
			
				return claims.getSubject();
	}
	
	
	
	
	public AuthProviderType getProviderTypeFromRegistrationId(String registrationId)
	{
		return switch(registrationId.toLowerCase())
				{
		case "google" -> AuthProviderType.GOOGLE;
		case "githumb" -> AuthProviderType.GITHUB;
		case "facebook" -> AuthProviderType.FACEBOOK;
		default -> throw new IllegalArgumentException("Unauthorized OAuth2 provider" +registrationId);
		
				};
	}
	
	
	
	public String determineProviderIdFromAuthUser(OAuth2User oauth2User, String registrationId)
	{
		String  providerId = switch (registrationId.toLowerCase())
				{
		case "google" -> oauth2User.getAttribute("sub");
		case "github" -> oauth2User.getAttribute("sub").toString();
		default ->{
	log.error("Unsupported OAuth2 provider : {}" , registrationId);
	throw new IllegalArgumentException("Unsupported OAuth2 provider " + registrationId);
				}
	};
	if(providerId == null || providerId.isBlank())
	{
		log.error("Unable to determine providerId for provider {} " , registrationId);
	   throw new IllegalArgumentException("Unable to determine providerId for OAuth2 login");
	}
	
	return providerId;
	

	
	}
	
	
	
	
	public String determineUsernameFromOAuth2User(OAuth2User oauth2User, String registrationId , String providerId)
	{
		
		
		String email = oauth2User.getAttribute("gmail");
		
		if(email != null && !email.isBlank())
		{
			return email;
		}
		 return  switch(registrationId.toLowerCase())
					{
					case "google" -> oauth2User.getAttribute("sum");
					case "githumb" -> oauth2User.getAttribute("login");
		default -> providerId;
	};
	
	
	
	

	}
}
