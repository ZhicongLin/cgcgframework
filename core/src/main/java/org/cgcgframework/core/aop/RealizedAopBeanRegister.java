package org.cgcgframework.core.aop;

import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.annotation.CInit;
import org.cgcgframework.core.aop.annotation.After;
import org.cgcgframework.core.aop.annotation.Aop;
import org.cgcgframework.core.aop.annotation.Around;
import org.cgcgframework.core.aop.annotation.Before;
import org.cgcgframework.core.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 * aop实现核心处理类
 */
@CBean
public class RealizedAopBeanRegister {

    private static Map<String, AopMethod> beforeMethods = new HashMap<>();
    private static Map<String, AopMethod> aroundMethods = new HashMap<>();
    private static Map<String, AopMethod> afterMethods = new HashMap<>();
    private static Map<String, AopMethod> aopMethods = new HashMap<>();
    private static Map<Class<?>, Object> aopContexts = new HashMap<>();

    @CInit(order = Integer.MIN_VALUE)
    public void initAop() {
        final Map<Class<?>, Object> context = ApplicationContext.getContext();
        // 获得全部注解aop的类
        context.forEach((k, v) -> {
            final Aop aop = k.getAnnotation(Aop.class);
            if (aop != null) {
                aopContexts.put(k, v);
            }
        });

        //解析注解Before，Around， After的方法
        this.analysis();
    }

    private void analysis() {
        aopContexts.forEach((k, v) -> {
            final Method[] methods = k.getMethods();
            for (Method method : methods) {
                final Before before = method.getDeclaredAnnotation(Before.class);
                if (before != null) {
                    this.registerMethod(k, v, method, before.value(), before.anno(), 1);
                    continue;
                }
                final Around around = method.getDeclaredAnnotation(Around.class);
                if (around != null) {
                    this.registerMethod(k, v, method, around.value(), around.anno(), 2);
                    continue;
                }
                final After after = method.getDeclaredAnnotation(After.class);
                if (after != null) {
                    this.registerMethod(k, v, method, after.value(), after.anno(), 3);
                }
            }
        });
    }

    private void registerMethod(Class<?> clz, Object bean, Method method, String value, Class anno, int type) {
        final Map<Class<?>, Object> context = ApplicationContext.getContext();
        context.forEach((k, v) -> {
            if (!anno.equals(Void.class)) {
                final Method[] methods = k.getMethods();
                for (Method mtd : methods) {
                    final Annotation annotation = mtd.getDeclaredAnnotation(anno);
                    if (annotation != null) {
                        if (type == 1) {
                            beforeMethods.put(mtd.toString(), new AopMethod(bean, method, type));
                        }
                        if (type == 2) {
                            aroundMethods.put(mtd.toString(), new AopMethod(bean, method, type));
                        }
                        if (type == 3) {
                            afterMethods.put(mtd.toString(), new AopMethod(bean, method, type));
                        }
                    }
                }
            }
        });
    }

    public static Map<String, AopMethod> getBeforeMethods() {
        return beforeMethods;
    }

    public static Map<String, AopMethod> getAroundMethods() {
        return aroundMethods;
    }

    public static Map<String, AopMethod> getAfterMethods() {
        return afterMethods;
    }
}