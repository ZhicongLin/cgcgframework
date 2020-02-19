package org.cgcgframework.core.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Setter
@Getter
@AllArgsConstructor
public class Initialization {

    private Object bean;
    private Method method;
    private int order;
}
