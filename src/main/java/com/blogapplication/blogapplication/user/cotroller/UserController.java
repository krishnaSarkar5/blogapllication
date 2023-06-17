package com.blogapplication.blogapplication.user.cotroller;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.user.dto.CreateUserRequestDto;
import com.blogapplication.blogapplication.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<ResponseDto> createUser(@RequestBody CreateUserRequestDto createUserRequestDto){
        ResponseDto responseDto = userService.createUser(createUserRequestDto);
        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<ResponseDto> getUser(@RequestHeader("Authorization") String Authorization){
        ResponseDto responseDto = userService.getUser();
        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
    }
}
