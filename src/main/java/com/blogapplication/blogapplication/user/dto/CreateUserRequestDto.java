package com.blogapplication.blogapplication.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateUserRequestDto {

    private String email;

    private String phone;

    private String firstName;

    private String lastName;

    private String password;


}
