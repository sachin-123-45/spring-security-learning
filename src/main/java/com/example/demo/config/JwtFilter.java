package com.example.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CostomUserDetailsService;
import com.example.demo.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	
	
	@Autowired
	private CostomUserDetailsService userDetailsService;
	
	
	@Autowired
	private  JwtService jwtservice;
	@Autowired
	private  UserRepository repo;
	
	
	private  HandlerExceptionResolver handlerExceptionResolver;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
		
		//log.info("incomming request : {} ", request.getRequestURI());
		
		final String requestTokenHeader = request.getHeader("Authorization");
		
		
		
		if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
			
		
		filterChain.doFilter(request, response);
		return;
		}
		
		
		String token = requestTokenHeader.split("Bearer ")[1];
		
		
		String username = jwtservice.getUsernameFromToken(token);
		
	if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
	{
		User user = repo.findByUserName(username).orElseThrow();
		UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken (user, null , null);
		
		SecurityContextHolder.getContext().setAuthentication(credentials);
		
		
	}
	
	filterChain.doFilter(request, response);
		
		
		}catch(Exception ex)
		{
		
		
		handlerExceptionResolver.resolveException(request, response, null, ex);
		
		}
	
	}
	
	
	
}
