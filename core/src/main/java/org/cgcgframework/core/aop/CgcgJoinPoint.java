package org.cgcgframework.core.aop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Setter
@Getter
@AllArgsConstructor
public class CgcgJoinPoint {
    private Object bean;
    private Method method;
    private Object[] args;
    private MethodProxy methodProxy;

    /**
     * 执行方法
     * @throws Throwable
     */
    public Object processor() throws Throwable {
        return methodProxy.invokeSuper(bean, args);
    }
    /**
     * 执行方法
     * @throws Throwable
     */
    public Object processor(Object... args) throws Throwable {
        return methodProxy.invokeSuper(bean, args);
    }
}
