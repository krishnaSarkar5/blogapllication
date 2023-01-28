package com.blogapplication.blogapplication.blog.dto.request;

import com.blogapplication.blogapplication.user.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetBlogResponseDto {


    private Long id;

    private String title;

    private String description;

    private String createdAt;

    private Integer status;

    private Boolean featured;

    private List<String> images;

    private Long createdBy;

    private Integer reaction;

    private boolean edited;

    private Integer views;

    // comment field yet to be added

}
