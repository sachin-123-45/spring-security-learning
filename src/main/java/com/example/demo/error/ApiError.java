package com.example.demo.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ApiError {
	
	
	private LocalDateTime timeStamp;
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	private String error;
	private HttpStatus statusCode;
	
	
	
	public ApiError() {
		
		this.timeStamp = LocalDateTime.now();
	}
	
	public ApiError(String error, HttpStatus statusCode) {
		this();
		this.error = error;
		this.statusCode = statusCode;
	}

}
