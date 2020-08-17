package com.sachin.example.simplegroovydsl.service;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sachin.example.simplegroovydsl.Constants;
import com.sachin.example.simplegroovydsl.core.AdapterContext;
import com.sachin.example.simplegroovydsl.core.AdapterDefinition;
import com.sachin.example.simplegroovydsl.core.AdapterExecutor;
import com.sachin.example.simplegroovydsl.exception.AdapterNotFoundException;
import com.sachin.example.simplegroovydsl.exception.AdapterParsedException;
import com.sachin.example.simplegroovydsl.model.DSLConfig;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdapterService  implements InitializingBean {

    private static final Splitter LINE_SPLITTER = Splitter.onPattern("\r?\n").omitEmptyStrings().trimResults();

    private final Map<String, AdapterExecutor> executors = new ConcurrentHashMap<>();


    public Object doAdapter(String name, Map<String, Object> input) {
        MDC.put(Constants.MDC_ADAPTER_ID, name);
        MDC.put(Constants.MDC_CUR_ADAPTER_ID, name);
        AdapterExecutor executor = executors.get(name);
        if (null == executor) {
            throw new AdapterNotFoundException(name);
        }
        return executor.doAdapter(input);
    }

    public AdapterExecutor resolveDsl(DSLConfig dslConfig) {
        log.info("收到dsl[{}]更新请求.\n content: {}", dslConfig.getName(), dslConfig.getContent());

        try {
            Preconditions.checkArgument(StringUtils.isNotBlank(dslConfig.getName()), "名称不能为空");
            Preconditions.checkArgument(StringUtils.isNotBlank(dslConfig.getContent()), "dsl代码不能为空");

            Closure closure = (Closure) new GroovyShell().parse(getMergedDslContent(dslConfig)).run();
            AdapterDefinition definition = new AdapterDefinition();
            closure.rehydrate(definition, definition, definition).run();

            if (!dslConfig.getScheduleEnabled() || !dslConfig.getEnabled()) {
                log.info("[{}]定时任务开关关闭.", dslConfig.getName());
                definition.setScheduleCall(null);
                AdapterExecutor.cancelScheduledTask(dslConfig.getName());
            }

            AdapterExecutor executor = null;
            if (dslConfig.getEnabled()) {
                executor = new AdapterExecutor(definition, dslConfig.getName());
                executors.put(dslConfig.getName(), executor);
            } else {
                executors.remove(dslConfig.getName());
            }
            log.info("初始化Adapter[{}]成功.", dslConfig.getName());
            return executor;
        } catch (Exception e) {
            log.info("初始化Adapter[{}]失败.", dslConfig.getName());
            throw new AdapterParsedException(dslConfig.getName(), e);
        }
    }

    /**
     * 默认导入的类
     * 可以通过配置的方式来实现动态改变
     *
     * @return
     */
    private List<String> getDefaultImportList() {
        return Lists.newArrayList("import org.slf4j.MDC");
    }

    private List<String> getMergedImportList(DSLConfig dslConfig) {
        List<String> list = new ArrayList<>();
        list.addAll(getDefaultImportList());
        if (!StringUtils.isNotBlank(dslConfig.getImportList())) {
            list.addAll(LINE_SPLITTER.splitToList(dslConfig.getImportList()));
            list = list.stream().distinct().collect(Collectors.toList());
        }
        return list;
    }

    private String getMergedDslContent(DSLConfig dslConfig) {
        List<String> importList = getMergedImportList(dslConfig);
        return String.format("%s\n return { %s }", Joiner.on("\n").join(importList), dslConfig.getContent());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AdapterContext.setExecutors(Collections.unmodifiableMap(executors));
    }
}
