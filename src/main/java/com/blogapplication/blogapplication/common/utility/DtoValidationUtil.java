package com.blogapplication.blogapplication.common.utility;

import java.util.Map;
import java.util.Objects;

public class DtoValidationUtil {

    public static void mandatoryCheck(String fieldName, String fieldValue, Map<String,Object> errorMap){

        if(Objects.isNull(fieldValue) || fieldValue.equalsIgnoreCase("")){
            errorMap.put(fieldName,"This filed can not be empty");
        }


    }
}
