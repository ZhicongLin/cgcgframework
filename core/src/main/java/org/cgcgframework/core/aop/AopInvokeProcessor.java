package org.cgcgframework.core.aop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;

public class AopInvokeProcessor {


    public static Object invokeSuper(Object o, Method method, Object[] objects, MethodProxy methodProxy,
                                     Map<String, AopMethod> beforeMethods, Map<String, AopMethod> aroundMethods, Map<String, AopMethod> afterMethods, String key) throws Throwable {
        AopMethod aopMethod = beforeMethods.get(key);
        if (aopMethod != null) {
            aopMethod.getMethod().invoke(aopMethod.getBean(), new JoinPoint(o, method, objects, methodProxy));
        }

        Object result;
        aopMethod = aroundMethods.get(key);
        if (aopMethod != null) {
            result = aopMethod.getMethod().invoke(aopMethod.getBean(), new JoinPoint(o, method, objects, methodProxy));
        } else {
            result = methodProxy.invokeSuper(o, objects);
        }

        aopMethod = afterMethods.get(key);
        if (aopMethod != null) {
            aopMethod.getMethod().invoke(aopMethod.getBean(), new JoinPoint(o, method, objects, methodProxy));
            return result;
        }

        return methodProxy.invokeSuper(o, objects);
    }

}
