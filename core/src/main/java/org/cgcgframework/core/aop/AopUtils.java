package org.cgcgframework.core.aop;

import com.sun.istack.internal.Nullable;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class AopUtils {

    @Nullable
    public static Object invokeJoinpointUsingReflection(@Nullable Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (methodProxy != null) {
            return methodProxy.invokeSuper(target, args);
        }
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(target, args);
    }

    public static boolean mathers(Method aopMethod, Method beanMethod, String executions) {
        return true;
    }
}
