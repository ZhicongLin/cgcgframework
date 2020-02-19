package com.cgcg.test;

import org.cgcgframework.core.aop.JoinPoint;
import org.cgcgframework.core.aop.annotation.After;
import org.cgcgframework.core.aop.annotation.Aop;
import org.cgcgframework.core.aop.annotation.Around;
import org.cgcgframework.core.aop.annotation.Before;

@Aop
public class AopTest {

    @Before(anno = TestAopAnno.class)
    public Object before(JoinPoint joinPoint) {
        System.out.println("before");
        return null;
    }
    @Around(anno = TestAopAnno.class)
    public Object around(JoinPoint joinPoint) throws Throwable {
        System.out.println("B = hehe2");
        final Object result = joinPoint.getMethodProxy().invokeSuper(joinPoint.getBean(), joinPoint.getArgs());
        System.out.println("A = hehe2");
        return result;
    }
    @After(anno = TestAopAnno.class)
    public Object after(JoinPoint joinPoint) {
        System.out.println("after");
        return null;
    }
}
