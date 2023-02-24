package com.blogapplication.blogapplication.blog.dto.response;

import lombok.Data;

@Data
public class ReplyResponseDto {

    private Long id;

    private Long blogId;

    private String reply;

    private String replyAt;

    private Long repliedBy;


    private Integer status;

    private boolean edited;
}
