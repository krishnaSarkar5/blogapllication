package com.blogapplication.blogapplication.blog.service;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.*;
import com.blogapplication.blogapplication.common.dto.requestDto.IdDto;

public interface BlogService {

    public ResponseDto createBlog(CreateBlogRequestDto request);

    public  ResponseDto getABlog(GetBlogRequestDto request);

    public ResponseDto getAllBlogs(GetAllBlogRequestDto requestDto);

    public  ResponseDto reactBlog(ReactBlogRequestDto request);

    public ResponseDto postComment(PostCommentRequestDto request);

    public ResponseDto replyComment(ReplyCommentRequestDto request);


    public ResponseDto getViews(IdDto idDto);

    public ResponseDto getReactions(IdDto idDto);
}
