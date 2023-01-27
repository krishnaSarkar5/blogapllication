package com.blogapplication.blogapplication.blog.service;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.CreateBlogRequestDto;
import com.blogapplication.blogapplication.blog.dto.request.GetBlogRequestDto;
import com.blogapplication.blogapplication.blog.dto.request.ReactBlogRequestDto;

public interface BlogService {

    public ResponseDto createBlog(CreateBlogRequestDto request);

    public  ResponseDto getABlog(GetBlogRequestDto request);

    public  ResponseDto reactBlog(ReactBlogRequestDto request);
}
