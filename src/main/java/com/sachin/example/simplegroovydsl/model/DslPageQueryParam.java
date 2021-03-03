package com.sachin.example.simplegroovydsl.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DslPageQueryParam extends Pageable {

    private String name;
    private String keyword;
    private Integer status;
}
