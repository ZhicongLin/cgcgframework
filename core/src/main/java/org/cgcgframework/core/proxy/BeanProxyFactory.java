package org.cgcgframework.core.proxy;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodProxy;
import org.cgcgframework.core.aop.AopMethod;
import org.cgcgframework.core.aop.AopUtils;
import org.cgcgframework.core.aop.AspectContext;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Getter
public abstract class BeanProxyFactory implements BeanProxyWare {
    private Enhancer enhancer = new Enhancer();

    public Object newProxyInstance(Class<?> clazz) {
        this.getEnhancer().setSuperclass(clazz);
        this.getEnhancer().setCallback(this);
        try {
            return this.getEnhancer().create();
        } catch (Exception e) {
            log.error("Register Bean Error: {} ", clazz);
            throw e;
        }
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        final Map<String, AopMethod> beforeMethods = AspectContext.beforeMethods;
        final Map<String, AopMethod> aroundMethods = AspectContext.aroundMethods;
        final Map<String, AopMethod> afterMethods = AspectContext.afterMethods;
        final String key = method.toString();
        if (beforeMethods.get(key) == null && aroundMethods.get(key) == null && afterMethods.get(key) == null) {
            //通过代理类调用父类中的方法
            return AopUtils.invokeJoinpointUsingReflection(o, method, objects, methodProxy);
        }
        return AopUtils.invokeJoinpointUsingReflection(o, method, objects, methodProxy);
    }

}
