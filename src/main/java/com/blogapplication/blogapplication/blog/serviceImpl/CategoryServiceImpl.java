package com.blogapplication.blogapplication.blog.serviceImpl;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.SaveCategoryRequestDto;
import com.blogapplication.blogapplication.blog.service.CategoryService;
import com.blogapplication.blogapplication.blog.serviceImpl.serviceMethods.GetCategoryService;
import com.blogapplication.blogapplication.blog.serviceImpl.serviceMethods.SaveCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private GetCategoryService getCategoryService;

    @Autowired
    private SaveCategoryService saveCategoryService;

    @Override
    public ResponseDto getAllCategories() {
        return getCategoryService.getAllCategories();
    }

    @Override
    public ResponseDto getACategoryById(Long id) {
        return getCategoryService.getACategoryById(id);
    }

    @Override
    public ResponseDto saveCategory(SaveCategoryRequestDto request) {
        return saveCategoryService.saveCategory(request);
    }
}
