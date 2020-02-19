package org.cgcgframework.core.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.cgcgframework.core.aop.AopInvokeProcessor;
import org.cgcgframework.core.aop.AopMethod;
import org.cgcgframework.core.aop.RealizedAopBeanRegister;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * javaBean 代理类
 */
public class BeanProxy implements MethodInterceptor {
    private Enhancer enhancer = new Enhancer();

    public Object getProxy(Class clazz) {
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        final Map<String, AopMethod> beforeMethods = RealizedAopBeanRegister.getBeforeMethods();
        final Map<String, AopMethod> aroundMethods = RealizedAopBeanRegister.getAroundMethods();
        final Map<String, AopMethod> afterMethods = RealizedAopBeanRegister.getAfterMethods();
        final String key = method.toString();
        if (beforeMethods.get(key) == null && aroundMethods.get(key) == null && afterMethods.get(key) == null) {
            //通过代理类调用父类中的方法
            return methodProxy.invokeSuper(o, objects);
        }
        return AopInvokeProcessor.invokeSuper(o, method, objects, methodProxy, beforeMethods, aroundMethods, afterMethods, key);
    }

}
