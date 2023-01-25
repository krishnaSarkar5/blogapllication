package com.blogapplication.blogapplication.common.exceptiom;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ServiceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private Map<String, String> indexError;
	private HttpStatus status;
	
	public ServiceException(String message) {
//		super(String.format(message));
		this.message=message;
		this.indexError = null;
		this.status = HttpStatus.BAD_REQUEST;
	}
	
	public ServiceException(String message, HttpStatus status) {
//		super(String.format(message));
		this.indexError = null;
		this.message=message;
		this.status = status;
	}

	public ServiceException(String message, Map<String, String> index) {
		this.message = message;
		this.indexError = index;
		this.status = HttpStatus.BAD_REQUEST;
	}
	
}
