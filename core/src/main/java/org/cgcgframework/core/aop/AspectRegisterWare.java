package org.cgcgframework.core.aop;

import org.aspectj.lang.annotation.Aspect;
import org.cgcgframework.core.CgcgScanBeanFactory;
import org.cgcgframework.core.CgcgScanner;
import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.annotation.CInit;
import org.cgcgframework.core.context.ApplicationContext;
import org.cgcgframework.core.proxy.CgcgAopProxy;

import java.lang.annotation.Annotation;

@CBean
public class AspectRegisterWare {

    @CInit(order = Integer.MIN_VALUE)
    public void register() {
        CgcgScanner.CLASS_HASH_SET.forEach(this::loadAspect);

        AspectContext.replaceContext.forEach(clazz -> {
            System.out.println("clazz ================================= " + clazz);
            Object newBean = new CgcgAopProxy().newProxyInstance(clazz);
            ApplicationContext.getContext().put(clazz, newBean);
        });
    }

    private void loadAspect(Class<?> clazz) {
        if (isBeanClass(clazz)) {
            final CgcgScanBeanFactory beanFactory = new CgcgScanBeanFactory();
            beanFactory.setClazz(clazz);
            beanFactory.setPkg(clazz.getPackage().getName());
            beanFactory.setBean(new CgcgAopProxy().newProxyInstance(clazz));
            ApplicationContext.putBean(beanFactory);
            AspectContext.analysis(clazz, beanFactory.getBean());
        }
    }

    private boolean isBeanClass(Class<?> clazz) {
        if (clazz.isInterface()) {
            return false;
        }

        final Aspect cbean = clazz.getAnnotation(Aspect.class);
        if (cbean != null) {
            return true;
        }

        final Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            final Class<? extends Annotation> aClass = annotation.annotationType();
            final Aspect bean = aClass.getAnnotation(Aspect.class);
            if (bean != null) {
                return true;
            }
        }
        return false;
    }
}
