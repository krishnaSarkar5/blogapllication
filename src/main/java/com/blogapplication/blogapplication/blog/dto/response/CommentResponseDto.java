package com.blogapplication.blogapplication.blog.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CommentResponseDto {

    private Long id;

    private Long blogId;

    private String comment;

    private String commentedAt;

    private Long commentedBy;

    private Integer status;

    private boolean edited;

    private List<CommentResponseDto> replies;
}
