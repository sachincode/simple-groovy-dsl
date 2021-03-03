package com.sachin.example.simplegroovydsl.enums;

import lombok.Getter;

@Getter
public enum DslStatusEnum {

    DISABLED(0, "已禁用"),
    ENABLED(1, "已启用"),
    DELETED(2, "已删除");

    private int code;
    private String desc;

    DslStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
