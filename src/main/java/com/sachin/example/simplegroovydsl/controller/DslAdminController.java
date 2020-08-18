package com.sachin.example.simplegroovydsl.controller;


import com.alibaba.fastjson.JSON;
import com.sachin.example.simplegroovydsl.model.DSLConfig;
import com.sachin.example.simplegroovydsl.model.DslPageQueryParam;
import com.sachin.example.simplegroovydsl.service.AdapterService;
import com.sachin.example.simplegroovydsl.service.DslAdminService;
import com.sachin.example.simplegroovydsl.service.api.Counter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.sachin.example.simplegroovydsl.Constants.BASE_PATH;


@Slf4j
@Api("DSL适配器接口")
@RestController
@RequestMapping(value = BASE_PATH + "/dsl/admin")
public class DslAdminController {


    @Autowired
    private AdapterService adapterService;
    @Autowired
    private DslAdminService dslAdminService;





    @ApiOperation(value = "发布DSL")
    @RequestMapping(value = "/publish/{operate}", method = {RequestMethod.POST})
    public Long publish(@PathVariable String operate, @RequestBody DSLConfig dslConfig) {
        log.info("发布DSL请求. {} config: {}", operate, JSON.toJSONString(dslConfig));
        return dslAdminService.publish(dslConfig, "new".equals(operate));
    }

    @ApiOperation(value = "查询所有的DSL")
    @RequestMapping(value = "/query", method = {RequestMethod.POST})
    public List<DSLConfig> publish(@RequestBody DslPageQueryParam param) {
        return dslAdminService.query(param);
    }

}
