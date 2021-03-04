package com.sachin.example.simplegroovydsl.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Map;

@Data
public class DslTestParam {


    private DSLConfig dslConfig;

    private boolean exEnabled;

    private Map<String, Object> body;
}
