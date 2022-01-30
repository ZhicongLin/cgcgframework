package org.cgcgframework.core.context;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cgcgframework.core.CgcgScanBeanFactory;
import org.cgcgframework.core.RegisterWare;
import org.cgcgframework.core.annotation.CValue;
import org.cgcgframework.core.annotation.Order;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class ApplicationContext {

    private static final Map<Class<?>, Object> context = new HashMap<>();

    private static final List<ApplicationInitializationWare> applicationInitializationWare = new ArrayList<>();

    private static final Set<String> keySet = new HashSet<>();

    private static final List<RegisterHandle> registers = new ArrayList<>();

    public static void putBean(CgcgScanBeanFactory beanFactory) {
        final Class<?> clazz = beanFactory.getClazz();
        if (keySet.contains(clazz.getName())) {
            return;
        }
        if (!ApplicationContextRegister.isBeanClass(clazz) && RegisterWare.class.isAssignableFrom(clazz)) {
            return;
        }
        keySet.add(clazz.getName());
        final Object bean = beanFactory.getBean();
        context.put(clazz, bean);
        if (bean instanceof ApplicationInitializationWare) {
            applicationInitializationWare.add((ApplicationInitializationWare) bean);
        } else if (bean instanceof RegisterWare) {
            final RegisterHandle handle = new RegisterHandle();
            handle.setRegister((RegisterWare) bean);
            final Order order = bean.getClass().getDeclaredAnnotation(Order.class);
            handle.setOrder(order != null ? order.value() : 0);
            registers.add(handle);
        }

        log.info("Initialization Bean for Class [{}]", clazz);
    }

    public static Map<Class<?>, Object> getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        final Object object = context.get(clazz);
        if (object != null) {
            return (T) object;
        }
        return (T) ApplicationContextRegister.loadBean(clazz);
    }

    public static List<ApplicationInitializationWare> getInitializations() {
        return applicationInitializationWare;
    }

    /**
     * 依赖注入
     */
    public static void injection() {
        //获取容器中全部已实例化的对象
        final Collection<Object> values = context.values();

        context.forEach((clazz, bean) -> {
            //实例化的对象全部属性
            final Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                final Resource resource = field.getAnnotation(Resource.class);
                if (resource == null) {
                    //其他的方式注入
                    otherInject(bean, field);
                } else {
                    //注解了resource
                    resourceInject(bean, field);
                }
            }
        });

        /*for (Object bean : values) {
            //实例化的对象全部属性
            final Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                final Resource resource = field.getAnnotation(Resource.class);
                if (resource == null) {
                    //其他的方式注入
                    otherInject(bean, field);
                } else {
                    //注解了resource
                    resourceInject(bean, field);
                }
            }
        }*/

    }

    private static void resourceInject(Object bean, Field field) {
        final Class<?> type = field.getType();
        final Object fieldBean = ApplicationContext.getBean(type);
        if (fieldBean == null) {
            return;
        }
        field.setAccessible(true);
        try {
            field.set(bean, fieldBean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void otherInject(Object bean, Field field) {
        final CValue cValue = field.getDeclaredAnnotation(CValue.class);
        if (cValue == null) {
            return;
        }
        String key = cValue.value();
        if (StringUtils.isBlank(key)) {
            key = field.getName();
        }
        final ApplicationProperties properties = ApplicationContext.getBean(ApplicationProperties.class);
        field.setAccessible(true);
        try {
            final Class<?> declaringClass = field.getType();
            field.set(bean, properties.get(key, declaringClass));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static List<RegisterHandle> getRegisters() {
        return registers;
    }

    @Setter
    @Getter
    public static class RegisterHandle {
        private RegisterWare register;
        private int order;
    }
}
