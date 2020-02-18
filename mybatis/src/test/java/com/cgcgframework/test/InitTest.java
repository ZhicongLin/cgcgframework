package com.cgcgframework.test;

import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.context.ApplicationInitializationWare;

@CBean
public class InitTest implements ApplicationInitializationWare {
    private Test test;

    public void initialization() {
        test.init();
    }
}
