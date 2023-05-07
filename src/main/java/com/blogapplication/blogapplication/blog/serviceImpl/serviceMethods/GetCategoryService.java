package com.blogapplication.blogapplication.blog.serviceImpl.serviceMethods;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.daoService.CategoryDaoService;
import com.blogapplication.blogapplication.blog.dto.response.CategoryResponseDto;
import com.blogapplication.blogapplication.blog.entity.BlogCategory;
import com.blogapplication.blogapplication.common.utility.ResponseUtil;
import com.blogapplication.blogapplication.common.utility.StatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GetCategoryService {

    public static final String CATEGORY_NOT_FOUND_MESSAGE = "No Category found with this id";
    @Autowired
    private StatusUtil statusUtil;

    @Autowired
    private CategoryDaoService categoryDaoService;

    @Autowired
    private ResponseUtil responseUtil;

    public ResponseDto getAllCategories(){

        List<BlogCategory> allCategoriesFromDB = getAllCategoriesFromDB();

        List<CategoryResponseDto> categoryResponseDtoList = allCategoriesFromDB.stream().map(c -> convertToResponseDto(c)).collect(Collectors.toList());

        return responseUtil.getSuccessResponse(categoryResponseDtoList);
    }



    public ResponseDto getACategoryById(Long id){

        Optional<BlogCategory> categoryOptional = getACategoryFromDB(id);

        if (categoryOptional.isEmpty()){
            return responseUtil.getFailureResponse(CATEGORY_NOT_FOUND_MESSAGE);
        }

        CategoryResponseDto categoryResponseDto = convertToResponseDto(categoryOptional.get());

        return responseUtil.getSuccessResponse(categoryResponseDto);
    }



    private List<BlogCategory> getAllCategoriesFromDB(){
        return categoryDaoService.getAllCategoryFromDB();
    }

    private Optional<BlogCategory> getACategoryFromDB(Long id){
        return categoryDaoService.getBlogById(id);
    }

    private CategoryResponseDto convertToResponseDto(BlogCategory category){
      return   CategoryResponseDto.builder()
              .id(category.getId())
              .image(category.getImage())
              .title(category.getTitle())
              .build();
    }
}
