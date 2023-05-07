package com.blogapplication.blogapplication.blog.serviceImpl.serviceMethods;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.daoService.CategoryDaoService;
import com.blogapplication.blogapplication.blog.dto.request.SaveCategoryRequestDto;
import com.blogapplication.blogapplication.blog.entity.BlogCategory;
import com.blogapplication.blogapplication.common.utility.ResponseUtil;
import com.blogapplication.blogapplication.common.utility.StatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveCategoryService {

    public static final String CATEGORY_SAVED = "Category saved";
    @Autowired
    private CategoryDaoService categoryDaoService;

    @Autowired
    private StatusUtil statusUtil;

    @Autowired
    private ResponseUtil responseUtil;

    public ResponseDto saveCategory(SaveCategoryRequestDto request){

        request.validate();

        BlogCategory blogCategory = BlogCategory.getCategoryInstanceFromDyo(request);

        blogCategory.setStatus(statusUtil.getActiveStatus());

        categoryDaoService.saveCategoryToDB(blogCategory);

        return responseUtil.getSuccessResponse(CATEGORY_SAVED);

    }


}
