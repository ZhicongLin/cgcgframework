package com.cgcg.test;

import org.cgcgframework.core.annotation.Application;

@Application
public class WebTest {
    public static void main(String[] args) throws Exception {
        org.cgcgframework.core.context.Application.run(WebTest.class);
    }
}
