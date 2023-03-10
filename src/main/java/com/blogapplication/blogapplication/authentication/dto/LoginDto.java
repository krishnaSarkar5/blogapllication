package com.blogapplication.blogapplication.authentication.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
	

	private String username;
	

	private String password;
	

	String channel;

}
