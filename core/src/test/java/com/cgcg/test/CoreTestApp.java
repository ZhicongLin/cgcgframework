package com.cgcg.test;

import org.cgcgframework.core.annotation.CStarter;
import org.cgcgframework.core.context.Application;

@CStarter
public class CoreTestApp {

    public static void main(String[] args) throws Exception {
        Application.run(CoreTestApp.class);
    }
}
