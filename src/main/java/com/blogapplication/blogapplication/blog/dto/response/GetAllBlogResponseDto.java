package com.blogapplication.blogapplication.blog.dto.response;

import com.blogapplication.blogapplication.blog.entity.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllBlogResponseDto {


    private Long id;

    private String title;

    private String description;

    private String createdAt;

    private Integer status;

    private Boolean featured;

    private List<String> images;

    private Long createdBy;

    private Integer ownReaction;

    private boolean edited;

    private Integer views;

    private Integer reactionCount;


    public GetAllBlogResponseDto(Blog blog){

        this.id = blog.getId();

        this.title = blog.getTitle();

        this.description = blog.getDescription();

        this.createdAt = blog.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        this.status = blog.getStatus();

        this.featured = blog.getFeatured();

        this.images = blog.getImages();

        this.createdBy = blog.getCreatedBy().getId();

//        this.ownReaction = b;

        this.edited = blog.getCreatedAt().equals(blog.getUpdatedAt());



    }


}
