package com.cgcg.jdbc;

import org.cgcgframework.core.annotation.CStarter;
import org.cgcgframework.core.context.Application;

@CStarter
public class JdbcStart {

    public static void main(String[] args) throws Exception {
        Application.run(JdbcStart.class, args);
    }
}
