package org.cgcgframework.core.event;

import org.cgcgframework.core.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventContext {

    private static final Map<Class<?>, Method> eventListeners = new HashMap<>();
    private static final Map<Method, Object> listenerContext = new HashMap<>();

    public static void put(Object bean, Class<?> eventClass, Method method) {
        Class<?> eClass = eventClass;
        if (eClass.equals(Void.class)) {
            final Parameter[] parameters = method.getParameters();
            if (parameters == null || parameters.length != 1) {
                return;
            }
            final Parameter parameter = parameters[0];
            eClass = parameter.getType();
        }
        eventListeners.put(eClass, method);
        listenerContext.put(method, bean);
    }

    public static Method getMethod(Class<?> eventClass) {
        return eventListeners.get(eventClass);
    }

    public static Object getBean(Method method) {
        return listenerContext.get(method);
    }

    /**
     * 初始化监听器
     */
    public static void initListener() {
        final Map<Class<?>, Object> context = ApplicationContext.getContext();
        final Set<Class<?>> classes = context.keySet();
        for (Class<?> aClass : classes) {
            final Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                final EventProcessor processor = method.getDeclaredAnnotation(EventProcessor.class);
                if (processor == null) {
                    continue;
                }
                EventContext.put(context.get(aClass), processor.value(), method);
            }
        }
    }
}
