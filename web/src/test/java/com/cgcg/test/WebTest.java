package com.cgcg.test;

import org.cgcgframework.core.annotation.CStarter;
import org.cgcgframework.core.context.Application;

@CStarter
public class WebTest {
    public static void main(String[] args) throws Exception {
        Application.run(WebTest.class);
    }
}
