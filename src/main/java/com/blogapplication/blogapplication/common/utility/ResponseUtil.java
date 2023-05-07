package com.blogapplication.blogapplication.common.utility;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    @Value("${successResponse}")
    private String defaultSuccessMessage;

    @Value("${failureResponse}")
    private String defaultFailureMessage;


    public ResponseDto getSuccessResponse(Object data){
        return  ResponseDto.builder()
                .status(true)
                .message(defaultSuccessMessage)
                .data(data)
                .build();
    }

    public ResponseDto getSuccessResponse(String message,Object data){
        return  ResponseDto.builder()
                .status(true)
                .message(message)
                .data(data)
                .build();
    }

    public ResponseDto getFailureResponse(String message){
        return  ResponseDto.builder()
                .status(false)
                .message(message)
                .data(null)
                .build();
    }

    public ResponseDto getFailureResponse(String message,Object data){
        return  ResponseDto.builder()
                .status(false)
                .message(message)
                .data(data)
                .build();
    }

    public ResponseDto getFailureResponse(){
        return  ResponseDto.builder()
                .status(false)
                .message(defaultFailureMessage)
                .data(null)
                .build();
    }
}
