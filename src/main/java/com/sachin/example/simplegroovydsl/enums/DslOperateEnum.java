package com.sachin.example.simplegroovydsl.enums;

import lombok.Getter;

@Getter
public enum DslOperateEnum {


    NEW_DSL("new", "新增", null),
    UPDATE_DSL("update", "更新", null),
    ENABLE_DSL("enable", "启用", DslStatusEnum.ENABLED),
    DISABLE_DSL("disable", "禁用", DslStatusEnum.DISABLED),
    ENABLE_TIMER("enable_timer", "启用定时任务", DslStatusEnum.ENABLED),
    DISABLE_TIMER("disable_timer", "禁用定时任务", DslStatusEnum.DISABLED);

    private String code;
    private String desc;
    private DslStatusEnum destStatus;

    DslOperateEnum(String code, String desc, DslStatusEnum destStatus) {
        this.code = code;
        this.desc = desc;
        this.destStatus = destStatus;
    }


    public static DslOperateEnum codeOf(String code) {
        for (DslOperateEnum anEnum : DslOperateEnum.values()) {
            if (anEnum.getCode().equalsIgnoreCase(code)) {
                return anEnum;
            }
        }
        return null;
    }
}
