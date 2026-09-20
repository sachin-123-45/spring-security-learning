package com.example.demo.config;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.dto.LoginResponse;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class Auth2SuccessHandler implements AuthenticationSuccessHandler {

	
	 private final UserService userService;
	 
	 private ObjectMapper objectMapper;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		
       OAuth2AuthenticationToken token =  (OAuth2AuthenticationToken) authentication;
       
      OAuth2User auth2user =  ( OAuth2User) authentication.getPrincipal();
      
      String retgistrationId = token.getAuthorizedClientRegistrationId();
      
      
   ResponseEntity<LoginResponse> loginResponse =   userService.handleOAuth2LoginRequest(auth2user,retgistrationId);
      
       
       response.setStatus(loginResponse.getStatusCode().value());
       response.setContentType(MediaType.APPLICATION_JSON_VALUE);
       response.getWriter().write(objectMapper.writeValueAsString(loginResponse.getBody()));
	}

}
