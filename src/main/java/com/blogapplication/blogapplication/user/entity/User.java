package com.blogapplication.blogapplication.user.entity;


import com.blogapplication.blogapplication.user.dto.CreateUserRequestDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email" ,columnDefinition = " VARCHAR(255) UNIQUE NOT NULL" ,unique = true)
    private String email;
    @Column(name = "phone",columnDefinition = " VARCHAR(255) UNIQUE ")
    private String phone;

    private String firstName;

    private String lastName;

    private String password;

    private String profileId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updatedAt;

    // 0 = inactive 1 = active
    private Integer status;

    private String role;


    public User(CreateUserRequestDto createUserRequestDto) {
        this.email = createUserRequestDto.getEmail();
        this.phone = createUserRequestDto.getPhone();
        this.firstName = createUserRequestDto.getFirstName();
        this.lastName = createUserRequestDto.getLastName();
        this.password = createUserRequestDto.getPassword();
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC"));
        this.updatedAt = LocalDateTime.now(ZoneId.of("UTC"));
        this.role = "ROLE_USER";
    }


}
