package com.sachin.example.simplegroovydsl.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel("分页参数")
public class Pageable {

    @ApiModelProperty("当前页码")
    private Integer page = 1;
    @ApiModelProperty("每页大小")
    private Integer size = 10;

    public Pageable(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }


    public int getOffset() {
        return this.page > 0 ? (this.page - 1) * this.size : 0;
    }

    public String getLimitSql() {
        return String.format(" limit %d offset %d ", this.size, getOffset());
    }


    public <T> List<T> getPageData(List<T> source) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        int offset = getOffset();
        if (offset >= source.size()) {
            return Collections.emptyList();
        }
        int endIndex = (offset + getSize()) < source.size() ? (offset + getSize()) : source.size();
        return source.subList(offset, endIndex);
    }


}
