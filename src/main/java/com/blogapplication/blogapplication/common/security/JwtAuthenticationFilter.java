package com.blogapplication.blogapplication.common.security;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blogapplication.blogapplication.authentication.dto.UserToken;
import com.blogapplication.blogapplication.authentication.entity.UserLoginToken;
import com.blogapplication.blogapplication.authentication.repository.UserLoginTokenRepository;
import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.blogapplication.blogapplication.user.entity.User;
import com.blogapplication.blogapplication.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;



import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUserDetailService jwtUserDetailService;
	
	@Autowired
	private JwtUtils jwtUtils; 
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserLoginTokenRepository userLoginTokenRepository;
	
	@Autowired
	private Environment environment;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String requestTokenHeader = request.getHeader("Authorization");
//		System.out.println(request.getRequestURI());
		String profileId = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				profileId = jwtUtils.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
	
		// Once we get the token validate it.
		if (profileId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			
			// this will come from respective service
			
			UserDetails userDetails = this.jwtUserDetailService.loadUserByUsername(profileId);
			User user = null;
			
			//validates if the username is phone number or email address
			user = userRepository.findByProfileId(profileId).orElseThrow(()->new ServiceException("User Not Founr"));
			
			UserLoginToken userLoginToken = userLoginTokenRepository.findByUser_idAndTokenAndStatus(user.getId(), requestTokenHeader, 1);
			boolean isTokenActive = !Objects.isNull(userLoginToken);
			

			
			
			boolean isTokenValidForActiveUsers = jwtUtils.validateTokenWExpirationValidation(jwtToken, userDetails)
					&& user.getStatus() == Integer.parseInt(environment.getProperty("active"));
			

			
			if (isTokenActive && isTokenValidForActiveUsers)
			{
				UserToken principal=new UserToken();
				principal.setId(user.getId());
				principal.setStatus(user.getStatus());
				principal.setUsername(Objects.isNull(user.getEmail())?user.getPhone():user.getEmail());
				principal.setChannel(userLoginToken.getChannel());
				principal.setUser(user);
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						principal, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		System.out.println("out of filter");
		filterChain.doFilter(request, response);
	}



}
