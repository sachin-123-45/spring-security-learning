package com.example.demo.dto;

public class LoginRequest {
	
	private String userName;
	private String password;
	public String getUsername() {
		return userName;
	}
	public LoginRequest(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}
	public void setUsername(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
