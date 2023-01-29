package com.blogapplication.blogapplication.blog.dto.request;


import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Data
public class PostCommentRequestDto {

    private Long blogId;

    private String comment;


    @JsonIgnore
    public void validateData(){

        Map<String ,String > errorMap = new HashMap<>();

        if(Objects.isNull(this.blogId) || this.blogId==0l){
            errorMap.put("blog id","INVALID BLOG ID");
        }

        if(Objects.isNull(this.comment) || !this.comment.trim().equals("")){
            errorMap.put("comment","INVALID COMMENT TEXT");
        }

        if(errorMap.size()>0){
            throw new ServiceException("INVALID_DATA",errorMap);
        }

    }
}
