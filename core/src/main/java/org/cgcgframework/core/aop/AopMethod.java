package org.cgcgframework.core.aop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Method;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class AopMethod {
    private Object bean;
    private Method method;
    private int type;
}
