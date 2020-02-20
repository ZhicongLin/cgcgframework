package org.cgcgframework.core.aop;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@CBean
@Slf4j
public class AspectContext {

    public static Map<String, AopMethod> beforeMethods = new HashMap<>();
    public static Map<String, AopMethod> aroundMethods = new HashMap<>();
    public static Map<String, AopMethod> afterMethods = new HashMap<>();
    public static Map<Class<?>, Object> aopContexts = new HashMap<>();
    public static Set<Class<?>> replaceContext = new HashSet<>();

    static void analysis(Class<?> k, Object v) {
        aopContexts.put(k, v);
        final Method[] methods = k.getMethods();
        for (Method method : methods) {
            final Before before = method.getDeclaredAnnotation(Before.class);
            if (before != null) {
                registerMethod(k, v, method, before.value(), 1);
                continue;
            }
            final Around around = method.getDeclaredAnnotation(Around.class);
            if (around != null) {
                registerMethod(k, v, method, around.value(), 2);
                continue;
            }
            final After after = method.getDeclaredAnnotation(After.class);
            if (after != null) {
                registerMethod(k, v, method, after.value(), 3);
            }
        }
    }

    private static void registerMethod(Class<?> clz, Object bean, Method method, String value, int type) {
        final Map<Class<?>, Object> context = ApplicationContext.getContext();
        context.forEach((k, v) -> {
            final Method[] methods = k.getMethods();
            boolean matcher = false;
            for (Method mtd : methods) {
                if (AopUtils.mathers(method, mtd, value)) {
                    if (type == 1) {
                        beforeMethods.put(mtd.toString(), new AopMethod(bean, method, type));
                    }
                    if (type == 2) {
                        aroundMethods.put(mtd.toString(), new AopMethod(bean, method, type));
                    }
                    if (type == 3) {
                        afterMethods.put(mtd.toString(), new AopMethod(bean, method, type));
                    }
                    matcher = true;
                }
            }

            if (matcher) {
                replaceContext.add(k);
            }
        });

    }
}
