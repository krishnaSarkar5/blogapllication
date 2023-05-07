package com.blogapplication.blogapplication.blog.dto.request;


import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.blogapplication.blogapplication.common.utility.DtoValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveCategoryRequestDto {

    private String title;

    private String image;


    public void validate(){
        Map<String,Object> errorMap = new HashMap<>();

        DtoValidationUtil.mandatoryCheck("title",title,errorMap);
        DtoValidationUtil.mandatoryCheck("image",image,errorMap);

        if(errorMap.size()>0){
            throw new ServiceException(errorMap);
        }
    }
}
