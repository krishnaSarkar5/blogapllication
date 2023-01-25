package com.blogapplication.blogapplication.authentication.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
	
	private String message;
	
	private Map<String, String> indexError;
}
