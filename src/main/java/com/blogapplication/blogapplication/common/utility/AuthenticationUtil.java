package com.blogapplication.blogapplication.common.utility;

import com.blogapplication.blogapplication.authentication.dto.UserToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationUtil {

	public UserToken currentLoggedInUser() {
		return (UserToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
