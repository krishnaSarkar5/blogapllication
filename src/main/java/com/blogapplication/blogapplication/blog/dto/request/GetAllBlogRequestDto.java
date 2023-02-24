package com.blogapplication.blogapplication.blog.dto.request;

import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Data
public class GetAllBlogRequestDto {

    private List<String> searchField;

    private List<Object> searchFieldValue;

    private Integer offset;

    private Integer pageSize;

    private String sortBy;


    public void validateData(){


        if(!this.searchField.isEmpty() && !this.searchFieldValue.isEmpty()
        && searchField.size()!=searchFieldValue.size()){
            throw new ServiceException("Size of search by field and value must be same");
        }

    }

}
