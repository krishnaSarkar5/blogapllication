package com.blogapplication.blogapplication.blog.service;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.*;

public interface BlogService {

    public ResponseDto createBlog(CreateBlogRequestDto request);

    public  ResponseDto getABlog(GetBlogRequestDto request);

    public  ResponseDto reactBlog(ReactBlogRequestDto request);

    public ResponseDto postComment(PostCommentRequestDto request);

    public ResponseDto replyComment(ReplyCommentRequestDto request);
}
