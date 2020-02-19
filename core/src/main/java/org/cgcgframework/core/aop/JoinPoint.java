package org.cgcgframework.core.aop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Setter
@Getter
@AllArgsConstructor
public class JoinPoint {
    private Object bean;
    private Method method;
    private Object[] args;
    private MethodProxy methodProxy;

}
