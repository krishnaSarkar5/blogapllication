package com.blogapplication.blogapplication.authentication.dto;

import com.blogapplication.blogapplication.user.entity.User;
import org.springframework.lang.NonNull;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserToken {
	
	@NonNull
	private long id;
	
	@NonNull
	private String username;
	
	@NonNull
	private int status;
	
	@NonNull
	private String channel;
	
	@NonNull
	private User user;

}
