package com.blogapplication.blogapplication.blog.serviceImpl;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.CreateBlogRequestDto;
import com.blogapplication.blogapplication.blog.entity.Blog;
import com.blogapplication.blogapplication.blog.repositoty.BlogRepository;
import com.blogapplication.blogapplication.blog.service.BlogService;
import com.blogapplication.blogapplication.common.utility.AuthenticationUtil;
import com.blogapplication.blogapplication.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private Environment environment;


    @Autowired
    private AuthenticationUtil authenticationUtil;

    @Override
    public ResponseDto createBlog(CreateBlogRequestDto request) {
        this.validateIncomingRequest(request);

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

    private void validateIncomingRequest(Object object){
        if(object instanceof CreateBlogRequestDto){
            CreateBlogRequestDto request =   (CreateBlogRequestDto) object;
            request.validateData();
        }
    }
}
