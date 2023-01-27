package com.blogapplication.blogapplication.blog.service;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.CreateBlogRequestDto;

public interface BlogService {

    public ResponseDto createBlog(CreateBlogRequestDto request);
}
