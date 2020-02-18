package com.cgcgframework.test;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface TestMapper {

    @Select("select * from steps_burying_point where id = #{id}")
    Map<String , Object> findById(Integer id);
}
