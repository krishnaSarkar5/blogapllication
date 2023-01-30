package com.blogapplication.blogapplication.blog.dto.response;

import lombok.Data;

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

    private Integer ownReaction;

    private boolean edited;

    private Integer views;

    private Integer reactionCount;

    private List<ReactionDto> reactionList;

    private List<CommentResponseDto> comments;

    // comment field yet to be added

}
