package com.blogapplication.blogapplication.user.serviceImpl.serviceMethods;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.*;
import com.blogapplication.blogapplication.blog.entity.Blog;
import com.blogapplication.blogapplication.blog.repositoty.BlogReactionDetailsRepository;
import com.blogapplication.blogapplication.blog.repositoty.BlogRepository;
import com.blogapplication.blogapplication.blog.repositoty.BlogViewDetailsRepository;
import com.blogapplication.blogapplication.blog.repositoty.CommentRepository;
import com.blogapplication.blogapplication.common.utility.AuthenticationUtil;
import com.blogapplication.blogapplication.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CreateBlog {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private Environment environment;


    @Autowired
    private AuthenticationUtil authenticationUtil;


    @Autowired
    private BlogReactionDetailsRepository blogReactedDetailsRepository;
    @Autowired
    private BlogViewDetailsRepository blogViewDetailsRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ValidateRequest validateRequest;

    public ResponseDto createNewBlog(CreateBlogRequestDto request) {
        validateRequest.validateIncomingRequest(request);

        User loggedInUser = authenticationUtil.currentLoggedInUser().getUser();

        Blog newBlogEntity = request.getNewBlogEntity();

        newBlogEntity.setCreatedBy(loggedInUser);
        newBlogEntity.setStatus(Integer.parseInt(environment.getProperty("active")));

        blogRepository.save(newBlogEntity);

        ResponseDto responseDto = new ResponseDto();

        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setMessage(environment.getProperty("blogCreated"));

        return responseDto;
    }



}
