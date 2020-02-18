package org.cgcgframework.core.aop;

import org.cgcgframework.core.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 描述:
 * aop实现核心处理类
 */
public class RealizedAopBeanPostProcessor {

    public static Object jdk(Class beanClass) {
        final InvocationHandler invocationHandler = (proxy, method, args) -> RealizedAopBeanPostProcessor.invoke(beanClass, method, args);
        return Proxy.newProxyInstance(beanClass.getClassLoader(), new Class[]{beanClass}, invocationHandler);
    }

    private static Object invoke(Class<?> beanClass, Method method, Object[] args) {
        try {
            System.out.println("beanClass = " + beanClass);
            return method.invoke(ApplicationContext.getBean(beanClass), args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}