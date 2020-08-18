package com.sachin.example.simplegroovydsl.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel("分页请求返回参数")
public class PageResponse<T> {

    @ApiModelProperty("当前页码")
    private Integer page;
    @ApiModelProperty("每页大小")
    private Integer size;
    @ApiModelProperty("总记录数")
    private Integer total;
    @ApiModelProperty("当前页数据")
    private List<T> elements;

    public PageResponse() {
        this.page = 0;
        this.size = 0;
        this.total = 0;
        this.elements = Collections.emptyList();
    }

    public PageResponse(Integer page, Integer size, Integer total, List<T> elements) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.elements = elements;
    }

    public PageResponse(Pageable pageable, Integer total, List<T> elements) {
        this.page = pageable.getPage();
        this.size = pageable.getSize();
        this.total = total;
        this.elements = elements;
    }
}
