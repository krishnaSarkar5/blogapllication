package com.blogapplication.blogapplication.user.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String phone;

    private String firstName;

    private String lastName;

    private String password;

    private String profileId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 0 = inactive 1 = active
    private Integer status;

    private String role;
}
