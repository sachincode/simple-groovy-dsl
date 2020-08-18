package com.sachin.example.simplegroovydsl.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.sachin.example.simplegroovydsl.dao.DslConfigEntityMapper;
import com.sachin.example.simplegroovydsl.dao.DslConfigHistoryEntityMapper;
import com.sachin.example.simplegroovydsl.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DslAdminService {


    @Autowired
    private DslConfigEntityMapper dslConfigEntityMapper;
    @Autowired
    private DslConfigHistoryEntityMapper dslConfigHistoryEntityMapper;
    @Autowired
    private AdapterService adapterService;


    public Long publish(DSLConfig dslConfig, boolean isNew) {
        checkParams(dslConfig, isNew);
        DslConfigEntity entity = buildDslConfigEntity(dslConfig, isNew);
        int r;
        if (isNew) {
            r = dslConfigEntityMapper.insertSelective(entity);
        } else {
            DslConfigEntityExample example = new DslConfigEntityExample();
            DslConfigEntityExample.Criteria criteria = example.createCriteria();
            criteria.andIdEqualTo(dslConfig.getId());
            criteria.andNameEqualTo(dslConfig.getName());
            criteria.andVersionEqualTo(dslConfig.getVersion());
            r = dslConfigEntityMapper.updateByExampleSelective(entity, example);
        }

        if (r == 0) {
            throw new RuntimeException(isNew ? "新增" : "更新" + "Dsl配置失败, 操作记录数为0.");
        }

        DslConfigHistoryEntity historyEntity = new DslConfigHistoryEntity();
        historyEntity.setConfig(JSON.toJSONString(entity));
        historyEntity.setName(entity.getName());
        historyEntity.setVersion(entity.getVersion());
        historyEntity.setOperator(LoginHelper.getLoginUsername());
        dslConfigHistoryEntityMapper.insertSelective(historyEntity);
        adapterService.resolveDsl(dslConfig);
        // 对于分布式环境下的发布，可以把配置同步到具备监听发布能力的服务上，集群下的机器监听配置的变化执行dsl初始化
        return entity.getId();
    }


    private void checkParams(DSLConfig dslConfig, boolean isNew) {
        Preconditions.checkArgument(isNew ? (dslConfig.getId() == null) : (dslConfig.getId() != null && dslConfig.getId() > 0), "参数ID非法");
        Preconditions.checkArgument(StringUtils.isNotBlank(dslConfig.getName()), "参数名称不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(dslConfig.getDescribe()), "参数描述不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(dslConfig.getContent()), "参数DSL代码不能为空");
        Preconditions.checkArgument(dslConfig.getEnabled() != null, "参数启用状态不能为空");
        Preconditions.checkArgument(dslConfig.getScheduleEnabled() != null, "参数定时任务状态不能为空");
        if (!isNew) {
            Preconditions.checkArgument(dslConfig.getVersion() != null && dslConfig.getVersion() > 0, "参数版本号不能为空");
        }
    }

    private DslConfigEntity buildDslConfigEntity(DSLConfig dslConfig, boolean isNew) {
        DslConfigEntity entity = new DslConfigEntity();
        entity.setContent(dslConfig.getContent());
        entity.setDescribe(dslConfig.getDescribe());
        entity.setImportList(dslConfig.getImportList() == null ? "" : dslConfig.getImportList());
        entity.setName(dslConfig.getName());
        entity.setScheduleStatus(dslConfig.getScheduleEnabled() ? 1 : 0);
        entity.setStatus(dslConfig.getEnabled() ? 1 : 0);
        entity.setVersion(isNew ? 1 : dslConfig.getVersion() + 1);
        entity.setOperator(LoginHelper.getLoginUsername());
        return entity;
    }

    public PageResponse<DSLConfig> pageQuery(DslPageQueryParam param) {
        DslConfigEntityExample example = new DslConfigEntityExample();
        DslConfigEntityExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(param.getName())) {
            criteria.andNameEqualTo(param.getName());
        }
        if (StringUtils.isNotBlank(param.getKeyWord())) {
            criteria.andContentLike(param.getKeyWord());
        }
        if (param.getStatus() != null && param.getStatus() >= 0) {
            criteria.andStatusEqualTo(param.getStatus());
        }
        long count = dslConfigEntityMapper.countByExample(example);
        List<DslConfigEntity> entityList = null;
        if (count > 0) {
            example.setOrderByClause(" gmt_modify desc " + param.getLimitSql());
            entityList = dslConfigEntityMapper.selectByExample(example);
        }
        return new PageResponse<>(param, (int) count, entityList == null ? null : entityList.stream().map(this::buildDSLConfig).collect(Collectors.toList()));
    }

    private DSLConfig buildDSLConfig(DslConfigEntity entity) {
        DSLConfig config = new DSLConfig();
        BeanUtils.copyProperties(entity, config);
        config.setEnabled(entity.getStatus() == 1);
        config.setScheduleEnabled(entity.getScheduleStatus() == 1);
        return config;
    }
}
