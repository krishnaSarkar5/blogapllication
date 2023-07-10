package com.blogapplication.blogapplication.blog.controller;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.*;
import com.blogapplication.blogapplication.blog.service.BlogService;
import com.blogapplication.blogapplication.common.dto.requestDto.IdDto;
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

    @PostMapping("/get-blog")
    public ResponseEntity<ResponseDto> getBlog(@RequestHeader("Authorization") String Authorization, @RequestBody GetBlogRequestDto getBlogRequestDto){
        ResponseDto responseDto = blogService.getABlog(getBlogRequestDto);
        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/react-blog")
    public ResponseEntity<ResponseDto> reactBlog(@RequestHeader("Authorization") String Authorization, @RequestBody ReactBlogRequestDto reactBlogRequestDto){
        ResponseDto responseDto = blogService.reactBlog(reactBlogRequestDto);
        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/post-comment")
    public ResponseEntity<ResponseDto> postComment(@RequestHeader("Authorization") String Authorization, @RequestBody PostCommentRequestDto postCommentRequestDto){
        ResponseDto responseDto = blogService.postComment(postCommentRequestDto);
        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/reply-comment")
    public ResponseEntity<ResponseDto> replyComment(@RequestHeader("Authorization") String Authorization, @RequestBody ReplyCommentRequestDto replyCommentRequestDto){
        ResponseDto responseDto = blogService.replyComment(replyCommentRequestDto);
        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
    }


    @PostMapping("/blog-views")
    public ResponseEntity<ResponseDto> getViews(@RequestHeader("Authorization") String Authorization, @RequestBody IdDto idDto){
        ResponseDto responseDto = blogService.getViews(idDto);
        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/trending-blogs")
    public ResponseEntity<ResponseDto> getAllTrendingBlog(){
        ResponseDto allTrendingBlog = blogService.getAllTrendingBlog();
        return  new ResponseEntity<>(allTrendingBlog,HttpStatus.OK);
    }


    @PostMapping("/all-blogs")
    public ResponseEntity<ResponseDto> getAllBlog(@RequestBody GetAllBlogRequestDto request){
        ResponseDto allTrendingBlog = blogService.getAllBlogs(request);
        return  new ResponseEntity<>(allTrendingBlog,HttpStatus.OK);
    }
}
