package com.blogapplication.blogapplication.authentication.service;

import com.blogapplication.blogapplication.authentication.dto.LoginDto;
import com.blogapplication.blogapplication.authentication.dto.ResponseDto;

public interface AuthenticationService {

    public ResponseDto loginWithPassword(LoginDto loginDto);

    public ResponseDto logout(String token);
}
