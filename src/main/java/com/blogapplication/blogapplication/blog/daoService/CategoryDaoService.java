package com.blogapplication.blogapplication.blog.daoService;

import com.blogapplication.blogapplication.blog.entity.BlogCategory;
import com.blogapplication.blogapplication.blog.repositoty.CategoryRepository;
import com.blogapplication.blogapplication.common.utility.StatusUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryDaoService {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StatusUtil statusUtil;


    public List<BlogCategory> getAllCategoryFromDB(){
        return categoryRepository.findAllByStatus(statusUtil.getActiveStatus());
    }

    public Optional<BlogCategory> getBlogById(Long id){
        return categoryRepository.findByIdAndStatus(id,statusUtil.getActiveStatus());
    }

    public void saveCategoryToDB(BlogCategory category){
        categoryRepository.save(category);
    }

}
