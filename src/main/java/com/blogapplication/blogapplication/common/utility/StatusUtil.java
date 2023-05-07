package com.blogapplication.blogapplication.common.utility;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StatusUtil {

    private Integer active;


    private Integer inactive;

    private Integer delete;


    public StatusUtil(@Value("${active}") Integer active,@Value("${inactive}") Integer inactive,@Value("${delete}") Integer delete) {
        this.active = active;
        this.inactive = inactive;
        this.delete = delete;
    }

    public Integer getActiveStatus(){
        return this.active;
    }
    public Integer getInactiveStatus(){
        return this.inactive;
    }
    public Integer getDeleteStatus(){
        return this.delete;
    }
}
