package com.blogapplication.blogapplication.common.dto.requestDto;

import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import lombok.Data;

import java.util.Objects;

@Data
public class IdDto {

    private Long id;


    public void validateData(){

        if(Objects.isNull(this.id)
        ||this.id==0){
            throw new ServiceException("INVALID_ID");
        }
    }
}
