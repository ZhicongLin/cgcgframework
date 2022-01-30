package org.cgcgframework.core.event;

import lombok.extern.slf4j.Slf4j;
import org.cgcgframework.core.thread.ThreadPoolManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 事件发送器
 *
 * @author zhicong.lin
 */
@Slf4j
public class EventPublisher {

    /**
     * 执行事件 （获取事件执行返回值（不支持异步））
     *
     * @return
     */
    public static <T> T execute(Object event, Class<T> returnType) {
        final Method method = EventContext.getMethod(event.getClass());
        if (method != null) {
            final Object bean = EventContext.getBean(method);
            if (bean != null) {
                try {
                    final Object invoke = method.invoke(bean, event);
                    final Class<?> invokeClass = invoke.getClass();
                    if (invokeClass.isAssignableFrom(returnType)) {
                        @SuppressWarnings("unchecked")
                        T t = (T) invoke;
                        return t;
                    } else {
                        throw new CastResultTypeException(invokeClass, returnType);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    /**
     * 发送事件 （不获取返回值（支持异步执行））
     */
    public static void push(Object event) {
        final Method method = EventContext.getMethod(event.getClass());
        if (method != null) {
            final Object bean = EventContext.getBean(method);
            if (bean != null) {
                try {
                    final Async async = method.getDeclaredAnnotation(Async.class);
                    if (async != null) {
                        asyncInvoke(event, method, bean);
                    } else {
                        method.invoke(bean, event);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private static void asyncInvoke(Object event, Method method, Object bean) {
        ThreadPoolManager.execute(() -> {
            try {
                method.invoke(bean, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

}
