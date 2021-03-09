package com.sachin.example.simplegroovydsl.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class DSLConfig {

    @ApiModelProperty("数据库唯一ID")
    private Long id;
    @ApiModelProperty("Adapter名称")
    private String name;
    @ApiModelProperty("Adapter描述")
    private String describe;
    @ApiModelProperty("Adapter是否启用")
    private Boolean enabled;
    @ApiModelProperty("Adapter定时任务是否启用")
    private Boolean scheduleEnabled;
    @ApiModelProperty("需要导入的类")
    private String importList;
    @ApiModelProperty("Adapter DSL")
    private String content;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("创建时间")
    private String gmtCreate;
    @ApiModelProperty("更新时间")
    private String gmtModify;

    private String opDesc;


}
