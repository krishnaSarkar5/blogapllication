package com.blogapplication.blogapplication.authentication.repository;

import com.blogapplication.blogapplication.authentication.entity.UserLoginToken;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserLoginTokenRepository extends JpaRepository<UserLoginToken, Long> {

	UserLoginToken findByUser_idAndTokenAndStatus(Long id, String requestTokenHeader, int i);
	
	UserLoginToken findByTokenAndStatus( String requestTokenHeader, int i);

}
