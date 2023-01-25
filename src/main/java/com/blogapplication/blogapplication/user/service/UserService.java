package com.blogapplication.blogapplication.user.service;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.user.dto.CreateUserRequestDto;

public interface UserService {

    public ResponseDto createUser(CreateUserRequestDto createUserRequestDto);
}
