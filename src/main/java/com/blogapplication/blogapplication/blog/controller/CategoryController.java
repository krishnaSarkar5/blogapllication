package com.blogapplication.blogapplication.blog.controller;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.SaveCategoryRequestDto;
import com.blogapplication.blogapplication.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseDto getAllCategory(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseDto getCategoryById(@PathVariable("id") Long id){
        return categoryService.getACategoryById(id);
    }

    @PostMapping
    public ResponseDto saveCategory(@RequestBody SaveCategoryRequestDto request){
        return categoryService.saveCategory(request);
    }

}
