package com.sachin.example.simplegroovydsl.controller;


import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.sachin.example.simplegroovydsl.enums.DslOperateEnum;
import com.sachin.example.simplegroovydsl.model.*;
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
    public ApiResponse<Long> publish(@PathVariable String operate, @RequestBody DSLConfig dslConfig) {
        log.info("发布DSL请求. {} config: {}", operate, JSON.toJSONString(dslConfig));
        DslOperateEnum operateEnum = DslOperateEnum.codeOf(operate);
        Preconditions.checkArgument(operateEnum != null, "不支持的操作命令");
        return ApiResponse.successOf(dslAdminService.publish(dslConfig, operateEnum));
    }

    @ApiOperation(value = "根据ID查询DSL")
    @RequestMapping(value = "/getDslById", method = {RequestMethod.GET})
    public ApiResponse<DSLConfig> getDslById(@RequestParam Long id) {
        return ApiResponse.successOf(dslAdminService.getDslById(id));
    }

    @ApiOperation(value = "查询所有的DSL")
    @RequestMapping(value = "/query", method = {RequestMethod.POST})
    public PageResponse<DSLConfig> pageQuery(@RequestBody DslPageQueryParam param) {
        return dslAdminService.pageQuery(param);
    }

    @ApiOperation(value = "查询所有的DSL名称")
    @RequestMapping(value = "/queryAllName", method = {RequestMethod.GET})
    public List<String> queryAllName() {
        return dslAdminService.queryAllName();
    }

    @ApiOperation(value = "删除指定的DSL")
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public ApiResponse<Boolean> deleteDslById(@RequestParam Long id) {
        return dslAdminService.deleteDslById(id);
    }


    @ApiOperation(value = "查询DSL历史操作记录")
    @RequestMapping(value = "/queryHistory", method = {RequestMethod.POST})
    public PageResponse<DslConfigHistoryEntity> queryHistory(@RequestBody DslPageQueryParam param) {
        return dslAdminService.queryHistory(param);
    }
}
