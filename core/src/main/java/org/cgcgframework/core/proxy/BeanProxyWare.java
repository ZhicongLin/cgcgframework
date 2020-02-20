package org.cgcgframework.core.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public interface BeanProxyWare extends MethodInterceptor {

    Enhancer getEnhancer();
}
