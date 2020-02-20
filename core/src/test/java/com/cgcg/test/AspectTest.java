package com.cgcg.test;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AspectTest {

    @Before("@annotation(aopAnno)")
    public void before(JoinPoint joinPoint, TestAopAnno aopAnno) {
        System.out.println("before");
    }

    @Around("@annotation(aopAnno)")
    public Object around(ProceedingJoinPoint pjp, TestAopAnno aopAnno) throws Throwable {
        System.out.println("B = hehe2");
        final Object result = pjp.proceed();
        System.out.println("A = hehe2");
        return result;
    }

    @After(value = "@annotation(aopAnno)", argNames = "aopAnno,joinPoint")
    public void after(TestAopAnno aopAnno, JoinPoint joinPoint) {
        System.out.println("after");
    }
}
