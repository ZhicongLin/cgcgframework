package com.cgcgframework.test;

import org.cgcgframework.core.annotation.CBean;

import java.util.Map;

@CBean
public class Test {

    private TestMapper testMapper;

    public void init() {
        final Map<String, Object> objectMap = testMapper.findById(8);
        System.out.println("objectMap = " + objectMap);
    }
}
