package com.blogapplication.blogapplication.blog.dto.request;

import com.blogapplication.blogapplication.blog.enums.BlogSearchField;
import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import lombok.Data;

import java.util.*;

@Data
public class GetAllBlogRequestDto {

    private List<String> searchField;

    private List<Object> searchFieldValue;

    private int offset;

    private int pageSize;

    private String sortBy;

    private String orderType;



    public void validateData(){


        Map<String,Object> errorObject =  new HashMap<>();


        if(!this.searchField.isEmpty() && !this.searchFieldValue.isEmpty()
        && searchField.size()!=searchFieldValue.size()){
            throw new ServiceException("Size of search by field and value must be same");
        }

        if(searchField.size()>0){
            List<String> inValidFields = new ArrayList<>();

            if(searchField.size()!=searchFieldValue.size()){
                errorObject.put("Search fields","Invalid field value. Mapping mismatch");
            }else {
                for(String field : searchField){
                    if(Objects.isNull(BlogSearchField.valueOf(field.toUpperCase()))){
                        inValidFields.add(field);
                    }
                }
            }



            if(inValidFields.size()>0){
                errorObject.put("Search fields","Invalid fields");
//            throw  new ServiceException(Arrays.toString(inValidFields.toArray(new String[0]))+" invalid field");
            }

        }




        if(!Objects.isNull(sortBy) && !sortBy.trim().equalsIgnoreCase("") && Objects.isNull(BlogSearchField.valueOf(sortBy.toUpperCase()))){
            errorObject.put("Sort By","Invalid sort by fields");
//            throw new ServiceException(sortBy+" invalid sort by field");
        }

        if(!Objects.isNull(orderType) && !orderType.trim().equalsIgnoreCase("") && !orderType.equalsIgnoreCase("ASC") && !orderType.equalsIgnoreCase("DESC")){
            errorObject.put("Order Type","Invalid orderType");
//            throw new ServiceException(orderType+" invalid orderType");
        }


        if(errorObject.size()>0){
            throw new ServiceException(errorObject);
        }


    }

}
