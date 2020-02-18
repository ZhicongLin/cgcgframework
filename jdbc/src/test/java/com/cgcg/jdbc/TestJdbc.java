package com.cgcg.jdbc;

import org.cgcgframework.jdbc.annotation.Jdbc;
import org.cgcgframework.jdbc.annotation.Sql;

import java.util.List;

@Jdbc
public interface TestJdbc {

    @Sql("select * from steps_burying_point where id > ?")
    List<?> count(Long id);
}
