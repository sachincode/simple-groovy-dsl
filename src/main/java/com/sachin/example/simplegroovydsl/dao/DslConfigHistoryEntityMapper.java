package com.sachin.example.simplegroovydsl.dao;

import com.sachin.example.simplegroovydsl.model.DslConfigHistoryEntity;
import com.sachin.example.simplegroovydsl.model.DslConfigHistoryEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface DslConfigHistoryEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_dsl_config_history
     *
     * @mbg.generated
     */
    @SelectProvider(type=DslConfigHistoryEntitySqlProvider.class, method="countByExample")
    long countByExample(DslConfigHistoryEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_dsl_config_history
     *
     * @mbg.generated
     */
    @DeleteProvider(type=DslConfigHistoryEntitySqlProvider.class, method="deleteByExample")
    int deleteByExample(DslConfigHistoryEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_dsl_config_history
     *
     * @mbg.generated
     */
    @Delete({
        "delete from T_dsl_config_history",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_dsl_config_history
     *
     * @mbg.generated
     */
    @Insert({
        "insert into T_dsl_config_history (gmt_create, gmt_modify, ",
        "name, config, operator, ",
        "version)",
        "values (#{gmtCreate,jdbcType=TIMESTAMP}, #{gmtModify,jdbcType=TIMESTAMP}, ",
        "#{name,jdbcType=VARCHAR}, #{config,jdbcType=VARCHAR}, #{operator,jdbcType=VARCHAR}, ",
        "#{version,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(DslConfigHistoryEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_dsl_config_history
     *
     * @mbg.generated
     */
    @InsertProvider(type=DslConfigHistoryEntitySqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insertSelective(DslConfigHistoryEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_dsl_config_history
     *
     * @mbg.generated
     */
    @SelectProvider(type=DslConfigHistoryEntitySqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modify", property="gmtModify", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="config", property="config", jdbcType=JdbcType.VARCHAR),
        @Result(column="operator", property="operator", jdbcType=JdbcType.VARCHAR),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER)
    })
    List<DslConfigHistoryEntity> selectByExample(DslConfigHistoryEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_dsl_config_history
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, gmt_create, gmt_modify, name, config, operator, version",
        "from T_dsl_config_history",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modify", property="gmtModify", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="config", property="config", jdbcType=JdbcType.VARCHAR),
        @Result(column="operator", property="operator", jdbcType=JdbcType.VARCHAR),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER)
    })
    DslConfigHistoryEntity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_dsl_config_history
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DslConfigHistoryEntitySqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") DslConfigHistoryEntity record, @Param("example") DslConfigHistoryEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_dsl_config_history
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DslConfigHistoryEntitySqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") DslConfigHistoryEntity record, @Param("example") DslConfigHistoryEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_dsl_config_history
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DslConfigHistoryEntitySqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(DslConfigHistoryEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_dsl_config_history
     *
     * @mbg.generated
     */
    @Update({
        "update T_dsl_config_history",
        "set gmt_create = #{gmtCreate,jdbcType=TIMESTAMP},",
          "gmt_modify = #{gmtModify,jdbcType=TIMESTAMP},",
          "name = #{name,jdbcType=VARCHAR},",
          "config = #{config,jdbcType=VARCHAR},",
          "operator = #{operator,jdbcType=VARCHAR},",
          "version = #{version,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(DslConfigHistoryEntity record);
}