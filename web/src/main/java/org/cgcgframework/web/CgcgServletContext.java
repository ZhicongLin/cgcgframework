package org.cgcgframework.web;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet上下文
 *
 * @author : zhicong.lin
 * @date : 2022/1/30 19:16
 */
@Slf4j
public class CgcgServletContext {

    private static final Map<String, Method> SERVLET_CONTEXT = new HashMap<>();
    private static final Map<Method, Object> BEAN_CONTEXT = new HashMap<>();

    public static void put(String uri, Method method, HttpMethod httpMethod, Object bean) {
        SERVLET_CONTEXT.put(httpMethod.name() + "::" + uri, method);
        BEAN_CONTEXT.put(method, bean);
        log.info("Mapped {}\t[{}] onto method [{}]", httpMethod.name(), uri, method);
    }

    public static Method getMethod(String uri, HttpMethod httpMethod) {
        return SERVLET_CONTEXT.get(httpMethod.name() + "::" + uri);
    }

    public static Object getBean(Method method) {
        return BEAN_CONTEXT.get(method);
    }
}
