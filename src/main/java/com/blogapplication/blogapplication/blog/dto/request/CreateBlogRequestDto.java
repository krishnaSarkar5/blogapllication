package com.blogapplication.blogapplication.blog.dto.request;


import com.blogapplication.blogapplication.blog.entity.Blog;
import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Data
public class CreateBlogRequestDto {

    private String title;

    private String description;


    private Boolean featured = false;

    private List<String> blogImageBase64Code;

    @JsonIgnore
    public void validateData(){

        Map<String,String> errorMap = new HashMap<>();

        if(Objects.isNull(this.title)){
            errorMap.put("title","Should not be empty or null");
        }

        if(Objects.isNull(this.description)){
            errorMap.put("description","Should not be empty or null");
        }

        if(errorMap.size()>0){
            throw new ServiceException("Invalid Data",errorMap);
        }
    }
    @JsonIgnore
    public Blog getNewBlogEntity(){

        Blog newBlog = new Blog();

        newBlog.setTitle(this.title);
        newBlog.setDescription(this.description);
        newBlog.setFeatured(this.featured);

        if(!this.blogImageBase64Code.isEmpty()){
            newBlog.setImages(this.blogImageBase64Code);
        }

        newBlog.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        newBlog.setUpdatedAt(newBlog.getCreatedAt());
        return newBlog;
    }
}
