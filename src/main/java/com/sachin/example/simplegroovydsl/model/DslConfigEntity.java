package com.sachin.example.simplegroovydsl.model;

import java.util.Date;

/**
 * Database Table Remarks:
 *   dsl配置表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table T_dsl_config
 */
public class DslConfigEntity {
    /**
     * Database Column Remarks:
     *   PK
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_dsl_config.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * Database Column Remarks:
     *   记录创建的物理时间戳
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_dsl_config.gmt_create
     *
     * @mbg.generated
     */
    private Date gmtCreate;

    /**
     * Database Column Remarks:
     *   记录修改的物理时间戳
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_dsl_config.gmt_modify
     *
     * @mbg.generated
     */
    private Date gmtModify;

    /**
     * Database Column Remarks:
     *   dsl名称编码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_dsl_config.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * Database Column Remarks:
     *   dsl描述
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_dsl_config.describe
     *
     * @mbg.generated
     */
    private String describe;

    /**
     * Database Column Remarks:
     *   dsl代码内容
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_dsl_config.content
     *
     * @mbg.generated
     */
    private String content;

    /**
     * Database Column Remarks:
     *   import列表
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_dsl_config.import_list
     *
     * @mbg.generated
     */
    private String importList;

    /**
     * Database Column Remarks:
     *   状态，0禁用1启用2删除
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_dsl_config.status
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * Database Column Remarks:
     *   定时任务状态，0禁用1启用
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_dsl_config.schedule_status
     *
     * @mbg.generated
     */
    private Integer scheduleStatus;

    /**
     * Database Column Remarks:
     *   操作人
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_dsl_config.operator
     *
     * @mbg.generated
     */
    private String operator;

    /**
     * Database Column Remarks:
     *   版本号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_dsl_config.version
     *
     * @mbg.generated
     */
    private Integer version;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_dsl_config.id
     *
     * @return the value of T_dsl_config.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_dsl_config.id
     *
     * @param id the value for T_dsl_config.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_dsl_config.gmt_create
     *
     * @return the value of T_dsl_config.gmt_create
     *
     * @mbg.generated
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_dsl_config.gmt_create
     *
     * @param gmtCreate the value for T_dsl_config.gmt_create
     *
     * @mbg.generated
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_dsl_config.gmt_modify
     *
     * @return the value of T_dsl_config.gmt_modify
     *
     * @mbg.generated
     */
    public Date getGmtModify() {
        return gmtModify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_dsl_config.gmt_modify
     *
     * @param gmtModify the value for T_dsl_config.gmt_modify
     *
     * @mbg.generated
     */
    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_dsl_config.name
     *
     * @return the value of T_dsl_config.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_dsl_config.name
     *
     * @param name the value for T_dsl_config.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_dsl_config.describe
     *
     * @return the value of T_dsl_config.describe
     *
     * @mbg.generated
     */
    public String getDescribe() {
        return describe;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_dsl_config.describe
     *
     * @param describe the value for T_dsl_config.describe
     *
     * @mbg.generated
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_dsl_config.content
     *
     * @return the value of T_dsl_config.content
     *
     * @mbg.generated
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_dsl_config.content
     *
     * @param content the value for T_dsl_config.content
     *
     * @mbg.generated
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_dsl_config.import_list
     *
     * @return the value of T_dsl_config.import_list
     *
     * @mbg.generated
     */
    public String getImportList() {
        return importList;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_dsl_config.import_list
     *
     * @param importList the value for T_dsl_config.import_list
     *
     * @mbg.generated
     */
    public void setImportList(String importList) {
        this.importList = importList;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_dsl_config.status
     *
     * @return the value of T_dsl_config.status
     *
     * @mbg.generated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_dsl_config.status
     *
     * @param status the value for T_dsl_config.status
     *
     * @mbg.generated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_dsl_config.schedule_status
     *
     * @return the value of T_dsl_config.schedule_status
     *
     * @mbg.generated
     */
    public Integer getScheduleStatus() {
        return scheduleStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_dsl_config.schedule_status
     *
     * @param scheduleStatus the value for T_dsl_config.schedule_status
     *
     * @mbg.generated
     */
    public void setScheduleStatus(Integer scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_dsl_config.operator
     *
     * @return the value of T_dsl_config.operator
     *
     * @mbg.generated
     */
    public String getOperator() {
        return operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_dsl_config.operator
     *
     * @param operator the value for T_dsl_config.operator
     *
     * @mbg.generated
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_dsl_config.version
     *
     * @return the value of T_dsl_config.version
     *
     * @mbg.generated
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_dsl_config.version
     *
     * @param version the value for T_dsl_config.version
     *
     * @mbg.generated
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}