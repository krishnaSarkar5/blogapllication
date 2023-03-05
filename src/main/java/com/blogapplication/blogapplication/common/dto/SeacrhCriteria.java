package com.blogapplication.blogapplication.common.dto;

import com.blogapplication.blogapplication.common.enums.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeacrhCriteria {

    private String field;

    private Object value;

//    private Operation operation;

}
