package com.blogapplication.blogapplication.blog.controller;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.CreateBlogRequestDto;
import com.blogapplication.blogapplication.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping("/create-blog")
    public ResponseEntity<ResponseDto> createBlog(@RequestHeader("Authorization") String Authorization, @RequestBody CreateBlogRequestDto createBlogRequestDto){
        ResponseDto responseDto = blogService.createBlog(createBlogRequestDto);
        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.CREATED);
    }
}
