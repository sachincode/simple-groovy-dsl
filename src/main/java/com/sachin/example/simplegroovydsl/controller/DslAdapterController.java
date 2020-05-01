package com.sachin.example.simplegroovydsl.controller;


import com.sachin.example.simplegroovydsl.model.DSLConfig;
import com.sachin.example.simplegroovydsl.service.AdapterService;
import com.sachin.example.simplegroovydsl.service.api.Counter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import static com.sachin.example.simplegroovydsl.Constants.BASE_PATH;


@Slf4j
@Api("DSL适配器接口")
@RestController
@RequestMapping(value = BASE_PATH + "/dsl")
public class DslAdapterController {


    @Autowired
    private AdapterService adapterService;
    @Autowired
    private Counter counter;


    @ApiOperation(value = "执行适配器")
    @RequestMapping(value = "/exec/{name}", method = {RequestMethod.POST, RequestMethod.GET})
    public Object doAdapter(@PathVariable String name, @RequestBody(required = false) Map<String, Object> input) {
        long startTime = System.currentTimeMillis();
        try {
            Object result = adapterService.doAdapter(name, CollectionUtils.isEmpty(input) ? Collections.emptyMap() : input);
            counter.inc(name, "success");
            return result;
        } catch (Exception e) {
            counter.inc(name, "failed");
            throw e;
        } finally {
            log.info("响应时间[{}] times:{}", name, System.currentTimeMillis() - startTime);
            counter.inc(name, "all");
        }
    }


    @ApiOperation(value = "测试发布适配器")
    @RequestMapping(value = "/publish", method = {RequestMethod.POST})
    public Object publish(@RequestBody DSLConfig dslConfig) {
        adapterService.resolveDsl(dslConfig);
        return true;
    }

}
