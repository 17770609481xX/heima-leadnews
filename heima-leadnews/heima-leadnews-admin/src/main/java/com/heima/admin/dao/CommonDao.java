package com.heima.admin.dao;

import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface CommonDao {

    /*无条件的查询所有方法*/
    @Select("select * from ${tableName} limit #{start},#{size}")
    @ResultType(HashMap.class)
    List<HashMap> list(@Param("tableName") String tableName, @Param("start")int start, @Param("size")int size);

    /*无条件查询数据条数*/
    @Select("select count(*) from ${tableName}")
    @ResultType(Integer.class)
    int listCount(@Param("tableName") String tableName);

    /*带条件分页查询*/
    @Select("select * from ${tableName} where 1=1 ${where} limit #{start},#{size}")
    @ResultType(HashMap.class)
    List<HashMap> listForWhere(@Param("tableName") String tableName,@Param("where") String where, @Param("start")int start, @Param("size")int size);

    /*带条件分页查询*/
    @Select("select count(*) from ${tableName} where 1=1 ${where}")
    @ResultType(Integer.class)
    int listCountForWhere(@Param("tableName") String tableName,@Param("where") String where);

    /*更新*/
    @Update("update ${tableName} set ${sets} where 1=1 ${where}")
    @ResultType(Integer.class)
    int update(@Param("tableName") String tableName,@Param("where")  String where,@Param("sets") String sets);

    /*新增*/
    @Insert("insert into ${tableName} (${fileds}) values (${values})")
    @ResultType(Integer.class)
    int insert(@Param("tableName") String tableName,@Param("fileds") String fileds,@Param("values") String values);

    /*删除*/
    @Delete("delete from ${tableName} where 1=1 ${where} limit 1")
    @ResultType(Integer.class)
    int delete(@Param("tableName") String tableName,@Param("where") String where);
}
