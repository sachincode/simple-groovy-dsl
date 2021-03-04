package com.sachin.example.simplegroovydsl.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sachin.example.simplegroovydsl.Constants;
import com.sachin.example.simplegroovydsl.dao.DslConfigEntityMapper;
import com.sachin.example.simplegroovydsl.dao.DslConfigHistoryEntityMapper;
import com.sachin.example.simplegroovydsl.enums.DslOperateEnum;
import com.sachin.example.simplegroovydsl.enums.DslStatusEnum;
import com.sachin.example.simplegroovydsl.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
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


    public Long publish(DSLConfig dslConfig, DslOperateEnum operateEnum) {
        boolean isNew = operateEnum == DslOperateEnum.NEW_DSL;
        checkParams(dslConfig, isNew);
        DslConfigEntity entity;
        if (operateEnum == DslOperateEnum.UPDATE_DSL || isNew || operateEnum == DslOperateEnum.ROLLBACK_DSL) {
            entity = buildDslConfigEntity(dslConfig, isNew);
            if (!isNew) {
                DslConfigEntity configEntity = dslConfigEntityMapper.selectByPrimaryKey(dslConfig.getId());
                Preconditions.checkArgument(dslConfig.getName().equals(configEntity.getName()), "DSL名称不能修改！");
                entity.setGmtCreate(configEntity.getGmtCreate());
                dslConfig.setVersion(configEntity.getVersion());
                entity.setVersion(configEntity.getVersion() + 1);
            }
        } else {
            entity = dslConfigEntityMapper.selectByPrimaryKey(dslConfig.getId());
            if (entity == null) {
                throw new RuntimeException("DSL不存在");
            }
            if (operateEnum == DslOperateEnum.ENABLE_DSL || operateEnum == DslOperateEnum.DISABLE_DSL) {
                entity.setStatus(operateEnum.getDestStatus().getCode());
            } else if (operateEnum == DslOperateEnum.ENABLE_TIMER || operateEnum == DslOperateEnum.DISABLE_TIMER) {
                entity.setScheduleStatus(operateEnum.getDestStatus().getCode());
            }
            dslConfig = buildDSLConfig(entity);
            entity.setVersion(entity.getVersion() + 1);
            entity.setOperator(LoginHelper.getLoginUsername());
            entity.setGmtModify(null);
        }
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
            entity.setId(dslConfig.getId());
            entity.setName(dslConfig.getName());
        }

        if (r == 0) {
            throw new RuntimeException(operateEnum.getDesc() + "Dsl配置失败, 操作记录数为0.");
        }

        insertHistory(entity, operateEnum);
        adapterService.resolveDsl(dslConfig);
        // 对于分布式环境下的发布，可以把配置同步到具备监听发布能力的服务上，集群下的机器监听配置的变化执行dsl初始化
        return entity.getId();
    }


    private void insertHistory(DslConfigEntity entity, DslOperateEnum operateEnum) {
        DslConfigHistoryEntity historyEntity = new DslConfigHistoryEntity();
        historyEntity.setConfig(JSON.toJSONString(entity));
        historyEntity.setName(entity.getName());
        historyEntity.setVersion(entity.getVersion());
        historyEntity.setOperator(entity.getOperator());
        historyEntity.setOpType(operateEnum.getCode());
        dslConfigHistoryEntityMapper.insertSelective(historyEntity);
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
        // todo name校验 必须是 字母、数字、下划线的组合，且必须为字母开头
    }

    private DslConfigEntity buildDslConfigEntity(DSLConfig dslConfig, boolean isNew) {
        DslConfigEntity entity = new DslConfigEntity();
        entity.setContent(dslConfig.getContent());
        entity.setDescribe(dslConfig.getDescribe());
        entity.setImportList(dslConfig.getImportList() == null ? "" : dslConfig.getImportList());
        entity.setScheduleStatus(dslConfig.getScheduleEnabled() ? 1 : 0);
        entity.setStatus(dslConfig.getEnabled() ? 1 : 0);
        entity.setVersion(isNew ? 1 : dslConfig.getVersion() + 1);
        entity.setOperator(LoginHelper.getLoginUsername());
        if (isNew) { // 不能更新name
            entity.setName(dslConfig.getName());
        }
        return entity;
    }

    public PageResponse<DSLConfig> pageQuery(DslPageQueryParam param) {
        DslConfigEntityExample example = new DslConfigEntityExample();
        DslConfigEntityExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(param.getName())) {
            criteria.andNameEqualTo(param.getName());
        }
        if (StringUtils.isNotBlank(param.getKeyword())) {
            criteria.andContentLike(param.getKeyword());
        }
        if (param.getStatus() != null && param.getStatus() >= 0) {
            criteria.andStatusEqualTo(param.getStatus());
        } else {
            criteria.andStatusNotEqualTo(DslStatusEnum.DELETED.getCode());
        }
        long count = dslConfigEntityMapper.countByExample(example);
        List<DslConfigEntity> entityList = null;
        if (count > 0) {
            example.setOrderByClause(" gmt_modify desc " + param.getLimitSql());
            entityList = dslConfigEntityMapper.selectByExample(example);
        }
        return new PageResponse<>(param, (int) count, entityList == null ? Lists.newArrayList() : entityList.stream().map(this::buildDSLConfig).collect(Collectors.toList()));
    }

    private DSLConfig buildDSLConfig(DslConfigEntity entity) {
        if (entity == null) {
            return null;
        }
        DSLConfig config = new DSLConfig();
        BeanUtils.copyProperties(entity, config);
        config.setEnabled(entity.getStatus() == 1);
        config.setScheduleEnabled(entity.getScheduleStatus() == 1);
        if (entity.getGmtCreate() != null) {
            config.setGmtCreate(DateFormatUtils.format(entity.getGmtCreate(), Constants.DATETIME_FORMAT));
        }if (entity.getGmtModify() != null) {
            config.setGmtModify(DateFormatUtils.format(entity.getGmtModify(), Constants.DATETIME_FORMAT));
        }
        return config;
    }

    public List<String> queryAllName() {
        List<String> list = dslConfigEntityMapper.selectNames();
        Collections.sort(list);
        return list;
    }

    public DSLConfig getDslById(Long id) {
        DslConfigEntity entity = dslConfigEntityMapper.selectByPrimaryKey(id);
        return buildDSLConfig(entity);
    }

    public ApiResponse<Boolean> deleteDslById(Long id) {
        DslConfigEntity entity = dslConfigEntityMapper.selectByPrimaryKey(id);
        if (entity == null) {
            throw new RuntimeException("DSL不存在");
        }
        entity.setOperator(LoginHelper.getLoginUsername());
        entity.setStatus(DslStatusEnum.DELETED.getCode());
        entity.setGmtModify(null);
        entity.setGmtModify(null);
        int update = dslConfigEntityMapper.updateByPrimaryKeySelective(entity);
        if (update == 0) {
            throw new RuntimeException("Dsl配置删除失败");
        }
        insertHistory(entity, DslOperateEnum.DELETE_DSL);

        DSLConfig dslConfig = new DSLConfig();
        dslConfig.setName(entity.getName());
        dslConfig.setContent(entity.getContent());
        dslConfig.setImportList(entity.getImportList());
        dslConfig.setScheduleEnabled(false);
        dslConfig.setEnabled(false);
        adapterService.resolveDsl(dslConfig);
        // 对于分布式环境下的发布，可以把配置同步到具备监听发布能力的服务上，集群下的机器监听配置的变化执行dsl初始化
        return ApiResponse.successOf(true);
    }



    @Scheduled(cron = "0/10 * * * * ?")
    public void execute() throws IOException {
        log.info("运行中的DSL：{}", adapterService.getAdapterExecutorNames());
    }


    public PageResponse<DslConfigHistoryEntity> queryHistory(DslPageQueryParam param) {
        DslConfigHistoryEntityExample example = new DslConfigHistoryEntityExample();
        DslConfigHistoryEntityExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(param.getName())) {
            criteria.andNameEqualTo(param.getName());
        }
        long count = dslConfigHistoryEntityMapper.countByExample(example);
        List<DslConfigHistoryEntity> entityList = Lists.newArrayList();
        if (count > 0) {
            example.setOrderByClause(" gmt_modify desc " + param.getLimitSql());
            entityList = dslConfigHistoryEntityMapper.selectByExample(example);
        }
        for (DslConfigHistoryEntity entity : entityList) {
            DslOperateEnum anEnum = DslOperateEnum.codeOf(entity.getOpType());
            if (anEnum != null) {
                entity.setOpType(anEnum.getDesc());
            }
        }
        return new PageResponse<>(param, (int) count, entityList);
    }


    public ApiResponse<Boolean> rollback(Long historyId) {
        DslConfigHistoryEntity historyEntity = dslConfigHistoryEntityMapper.selectByPrimaryKey(historyId);
        Preconditions.checkNotNull(historyEntity, "历史记录不存在");
        DSLConfig config = JSON.parseObject(historyEntity.getConfig(), DSLConfig.class);
        config.setEnabled(true);
        config.setScheduleEnabled(false);
        publish(config, DslOperateEnum.ROLLBACK_DSL);
        return ApiResponse.successOf(null);
    }
}
