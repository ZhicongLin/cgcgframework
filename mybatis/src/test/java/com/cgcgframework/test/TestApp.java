package com.cgcgframework.test;

import org.cgcgframework.core.annotation.Application;

@Application
public class TestApp {

    public static void main(String[] args) throws Exception {
        org.cgcgframework.core.context.Application.run(TestApp.class, args);
    }
}
