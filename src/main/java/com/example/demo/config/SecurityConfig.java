package com.example.demo.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {
	
	
	private final JwtFilter jwtFilter;
	
	private final  Auth2SuccessHandler auth2ScuccessHandler;
	
	

	
	
	@Bean 
	public SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception
	{
		
		http.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/**")
				.permitAll()
				.anyRequest()
				.authenticated()
				
				)
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
		.oauth2Login(oAuth2-> oAuth2
				.failureHandler((HttpServletRequest request, HttpServletResponse response,
				AuthenticationException exception) ->{
					
					   System.out.println("OAuth2 login failed: " + exception.getMessage());
					  
				})
				
			.successHandler(auth2ScuccessHandler)
						
						
					
				
					
					
						
					
				
				);
		//formLogin(Customizer.withDefaults());
		
		return http.build();
		
	}
	

}
