package com.blogapplication.blogapplication.user.serviceImpl.serviceMethods;

import com.blogapplication.blogapplication.blog.dto.request.*;
import com.blogapplication.blogapplication.common.dto.requestDto.IdDto;
import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import org.springframework.stereotype.Component;


@Component
public class ValidateRequest {

     void validateIncomingRequest(Object object){
        if(object instanceof CreateBlogRequestDto){
            CreateBlogRequestDto request =   (CreateBlogRequestDto) object;
            request.validateData();
        }

        else if (object instanceof GetBlogRequestDto) {
            GetBlogRequestDto request =   (GetBlogRequestDto) object;
            request.validateData();
        }

        else if (object instanceof ReactBlogRequestDto) {
            ReactBlogRequestDto request =   (ReactBlogRequestDto) object;
            request.validateData();
        }

        else if (object instanceof PostCommentRequestDto) {
            PostCommentRequestDto request = (PostCommentRequestDto) object;
            request.validateData();
        }

        else if (object instanceof ReplyCommentRequestDto) {
            ReplyCommentRequestDto request = (ReplyCommentRequestDto) object;
            request.validateData();
        }
        else if (object instanceof GetAllBlogRequestDto) {
            GetAllBlogRequestDto request = (GetAllBlogRequestDto) object;
            request.validateData();
        }

        else if (object instanceof IdDto) {
            IdDto request = (IdDto) object;
            request.validateData();
        }
        else {
            throw new ServiceException("PROCESS_ERROR");
        }
    }
}
