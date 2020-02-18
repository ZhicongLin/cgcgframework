package org.cgcgframework.web;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CgcgServletContext {

    private static final Map<String, Method> servletContext = new HashMap<>();
    private static final Map<Method, Object> beanContext = new HashMap<>();

    public static void put(String uri, Method method, HttpMethod httpMethod, Object bean) {
        servletContext.put(httpMethod.name() + "::" + uri, method);
        beanContext.put(method, bean);
        log.info("Mapped URL path [{}] onto method [{}]", uri, method);
    }

    public static Method getMethod(String uri, HttpMethod httpMethod) {
        return servletContext.get(httpMethod.name() + "::" + uri);
    }

    public static Object getBean(Method method) {
        return beanContext.get(method);
    }
}
