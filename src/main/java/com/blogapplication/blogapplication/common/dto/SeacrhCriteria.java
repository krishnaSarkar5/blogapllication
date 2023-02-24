package com.blogapplication.blogapplication.common.dto;

import com.blogapplication.blogapplication.common.enums.Operation;
import lombok.Data;

@Data
public class SeacrhCriteria {

    private String field;

    private String value;

    private Operation operation;
}
