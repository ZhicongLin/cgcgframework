package com.cgcg.jdbc;

import org.cgcgframework.core.annotation.Application;

@Application
public class JdbcStart {

    public static void main(String[] args) throws Exception {
        org.cgcgframework.core.context.Application.run(JdbcStart.class, args);
    }
}
