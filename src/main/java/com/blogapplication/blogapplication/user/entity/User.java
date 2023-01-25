package com.blogapplication.blogapplication.user.entity;


import com.blogapplication.blogapplication.user.dto.CreateUserRequestDto;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email",unique = true,nullable = false)
    private String email;
    @Column(name = "phone",unique = true)
    private String phone;

    private String firstName;

    private String lastName;

    private String password;

    private String profileId;

    private LocalDateTime createdAt;

    public User(CreateUserRequestDto createUserRequestDto) {
        this.email = createUserRequestDto.getEmail();
        this.phone = createUserRequestDto.getPhone();
        this.firstName = createUserRequestDto.getFirstName();
        this.lastName = createUserRequestDto.getFirstName();
        this.password = createUserRequestDto.getPassword();
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC"));
        this.updatedAt = LocalDateTime.now(ZoneId.of("UTC"));
        this.role = "ROLE_USER";
    }

    private LocalDateTime updatedAt;

    // 0 = inactive 1 = active
    private Integer status;

    private String role;
}
