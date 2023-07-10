package com.blogapplication.blogapplication.blog.serviceImpl.serviceMethods;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.daoService.CategoryDaoService;
import com.blogapplication.blogapplication.blog.dto.request.*;
import com.blogapplication.blogapplication.blog.entity.Blog;
import com.blogapplication.blogapplication.blog.entity.BlogCategory;
import com.blogapplication.blogapplication.blog.repositoty.BlogReactionDetailsRepository;
import com.blogapplication.blogapplication.blog.repositoty.BlogRepository;
import com.blogapplication.blogapplication.blog.repositoty.BlogViewDetailsRepository;
import com.blogapplication.blogapplication.blog.repositoty.CommentRepository;
import com.blogapplication.blogapplication.common.cloudservice.service.UploadFileService;
import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.blogapplication.blogapplication.common.utility.AuthenticationUtil;
import com.blogapplication.blogapplication.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerErrorException;

import java.util.Map;
import java.util.UUID;

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

    @Autowired
    private CategoryDaoService categoryDaoService;

    @Autowired
    private UploadFileService uploadFileService;

    public ResponseDto createNewBlog(CreateBlogRequestDto request) {
        validateRequest.validateIncomingRequest(request);

        User loggedInUser = authenticationUtil.currentLoggedInUser().getUser();

        Blog newBlogEntity = request.getNewBlogEntity(getCategoryById(request.getCategoryId()));

        String image = uploadImage(request.getBlogImageBase64Code());

        newBlogEntity.setImage(image);

        newBlogEntity.setCreatedBy(loggedInUser);
        newBlogEntity.setStatus(Integer.parseInt(environment.getProperty("active")));

        blogRepository.save(newBlogEntity);

        ResponseDto responseDto = new ResponseDto();

        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setMessage(environment.getProperty("blogCreated"));

        return responseDto;
    }


    private BlogCategory getCategoryById(Long id){
        return categoryDaoService.getBlogById(id).orElseThrow(()->new ServiceException(Map.of("message","Invalid category id")));
    }


    private String uploadImage(String base64code){
        return uploadFileService.uploadImage(base64code, UUID.randomUUID().toString(),"Blog");
    }
}
