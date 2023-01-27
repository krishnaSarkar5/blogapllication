package com.blogapplication.blogapplication.blog.dto.request;


import com.blogapplication.blogapplication.common.dto.requestDto.IdDto;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class ReactBlogRequestDto extends IdDto {

    private Integer reactionValue ;


    private boolean isReacted;

    public void validateData(){

        super.validateData();
    }

}
