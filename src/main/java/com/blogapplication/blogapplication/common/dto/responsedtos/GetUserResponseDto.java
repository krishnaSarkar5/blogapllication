package com.blogapplication.blogapplication.common.dto.responsedtos;

import com.blogapplication.blogapplication.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponseDto {



    private Long id;

    private String email;

    private String phone;

    private String firstName;

    private String lastName;

    private String profileId;


    private Integer status;

    private String role;


    public GetUserResponseDto(User user){
        this.id= user.getId();

        this.email = user.getEmail();

        this.phone = user.getPhone();

        this.firstName = user.getFirstName();

        this.lastName = user.getLastName();

        this.profileId = user.getProfileId();


        this.status = user.getStatus();

        this.role = user.getRole();
    }
}
