package com.blogapplication.blogapplication.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllBlogResponseWithCountDto {

    private Integer count;

    private List<GetAllBlogResponseDto> blogList;
}
