package com.blogapplication.blogapplication.blog.dto.request;

import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class ReplyCommentRequestDto {

    private Long commentId;

    private String reply;

    @JsonIgnore
    public void validateData(){

        Map<String ,Object > errorMap = new HashMap<>();

        if(Objects.isNull(this.commentId) || this.commentId==0l){
            errorMap.put("blog id","INVALID COMMENT ID");
        }

        if(Objects.isNull(this.reply) || this.reply.trim().equals("")){
            errorMap.put("comment","INVALID REPLY TEXT");
        }

        if(errorMap.size()>0){
            throw new ServiceException("INVALID_DATA",errorMap);
        }

    }
}
