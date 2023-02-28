package com.blogapplication.blogapplication.blog.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GetAllBlogResponseWithCountDto {

    private Integer count;

    private List<GetAllBlogResponseDto> blogList;
}
