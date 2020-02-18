package org.cgcgframework.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cgcgframework.core.ApplicationRegister;
import org.cgcgframework.core.CgcgScanBeanFactory;
import org.cgcgframework.core.CgcgScanner;
import org.cgcgframework.core.RegisterWare;
import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.context.ApplicationContext;
import org.cgcgframework.web.annotation.CApi;
import org.cgcgframework.web.annotation.mapping.Mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

@CBean
public class MvcRegister implements RegisterWare {


    public void register(CgcgScanner scanner) {
        for (String pkg : ApplicationRegister.getPkgs()) {
            final Set<Class<?>> contexts = scanner.scan(pkg);
            for (Class<?> context : contexts) {
                final CApi cApi = context.getAnnotation(CApi.class);
                if (cApi != null) {
                    initBean(context);
                }
            }
        }
    }

    private void initBean(Class clazz) {
        final CgcgScanBeanFactory beanFactory = new CgcgScanBeanFactory(clazz);
        ApplicationContext.putBean(beanFactory);
        final Object bean = beanFactory.getBean();
        if (bean != null) {
            final Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                final MappingHandle mapping = getMappingHandle(method);
                if (mapping == null) {
                    continue;
                }
                CgcgServletContext.put(mapping.getKey(), method, mapping.getMapping().httpMethod(), bean);
            }
        }
    }

    private MappingHandle getMappingHandle(Method method) {
        final Annotation[] annotations = method.getAnnotations();
        if (annotations == null || annotations.length == 0) {
            return null;
        }
        for (Annotation annotation : annotations) {
            final Class<? extends Annotation> type = annotation.annotationType();
            if (!type.equals(Mapping.class)) {
                final Mapping mapping = type.getAnnotation(Mapping.class);
                if (mapping != null) {
                    final Method[] methods = annotation.getClass().getMethods();
                    for (Method mtd : methods)
                        if (mtd.getName().equals("value")) {
                            final Object invoke;
                            try {
                                invoke = mtd.invoke(annotation);
                                return new MappingHandle(invoke.toString(), mapping);
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                }
            } else {
                final Mapping mapping = (Mapping) annotation;
                return new MappingHandle(mapping.value(), mapping);
            }
        }
        return null;
    }

    @AllArgsConstructor
    @Getter
    private class MappingHandle {
        String key;
        Mapping mapping;
    }
}
