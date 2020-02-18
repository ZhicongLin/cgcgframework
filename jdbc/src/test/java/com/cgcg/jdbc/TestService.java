package com.cgcg.jdbc;

import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.annotation.CInit;

import javax.annotation.Resource;

@CBean
public class TestService {

    @Resource
    private TestJdbc testJdbc;

    @CInit
    public void init() {
        System.out.println("count = " + testJdbc.count(6L));
    }
    @CInit
    public void init2() {
        System.out.println("count2 = " + testJdbc.count(6L));
    }
    @CInit
    public void init3() {
        System.out.println("count2 = " + testJdbc.count(3L));
    }
}
