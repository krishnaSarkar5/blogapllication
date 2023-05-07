package com.blogapplication.blogapplication.blog.service;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.SaveCategoryRequestDto;

public interface CategoryService {

    ResponseDto getAllCategories();

    ResponseDto getACategoryById(Long id);

    ResponseDto saveCategory(SaveCategoryRequestDto request);
}
