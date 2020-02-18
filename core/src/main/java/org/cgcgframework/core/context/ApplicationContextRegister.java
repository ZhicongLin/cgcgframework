package org.cgcgframework.core.context;

import org.cgcgframework.core.CgcgScanBeanFactory;
import org.cgcgframework.core.CgcgScanner;
import org.cgcgframework.core.RegisterWare;
import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.annotation.Order;

import java.lang.annotation.Annotation;
import java.util.Set;

@Order
@CBean
public class ApplicationContextRegister implements RegisterWare {

    @Override
    public void register() {
        final Set<Class<?>> contexts = CgcgScanner.CLASS_HASH_SET;
        for (Class<?> context : contexts) {
            loadBean(context);
        }
    }

    static Object loadBean(Class<?> clazz) {
        if (!isBeanClass(clazz)) {
            return null;
        }
        final CgcgScanBeanFactory beanFactory = new CgcgScanBeanFactory(clazz);
        ApplicationContext.putBean(beanFactory);
        if (ApplicationPropertiesWare.class.isAssignableFrom(clazz)) {
            ((ApplicationPropertiesWare) beanFactory.getBean()).load(clazz, ApplicationContext.getBean(ApplicationProperties.class));
        }
        return beanFactory.getBean();
    }

    static boolean isBeanClass(Class<?> clazz) {
        if (clazz.isInterface()) {
            return false;
        }

        final CBean cbean = clazz.getAnnotation(CBean.class);
        if (cbean != null) {
            return true;
        }

        final Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            final Class<? extends Annotation> aClass = annotation.annotationType();
            final CBean bean = aClass.getAnnotation(CBean.class);
            if (bean != null) {
                return true;
            }
        }
        return false;
    }
}
