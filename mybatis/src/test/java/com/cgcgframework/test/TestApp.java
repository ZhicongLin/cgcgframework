package com.cgcgframework.test;

import org.cgcgframework.core.annotation.CStarter;
import org.cgcgframework.core.context.Application;

@CStarter
public class TestApp {

    public static void main(String[] args) throws Exception {
        Application.run(TestApp.class, args);
    }
}
