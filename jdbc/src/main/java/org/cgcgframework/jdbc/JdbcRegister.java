package org.cgcgframework.jdbc;

import org.cgcgframework.core.ApplicationRegister;
import org.cgcgframework.core.CgcgScanBeanFactory;
import org.cgcgframework.core.CgcgScanner;
import org.cgcgframework.core.RegisterWare;
import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.context.ApplicationContext;
import org.cgcgframework.jdbc.annotation.Jdbc;
import org.cgcgframework.jdbc.proxy.JdbcFactoryBean;

import java.lang.reflect.Method;
import java.util.Set;

@CBean
public class JdbcRegister implements RegisterWare {


    /**
     * 注册方法
     * @param scanner
     * @return void
     * @author : zhicong.lin
     * @date : 2022/1/30 15:51
     */
    @Override
    public void register(CgcgScanner scanner) {
        for (String pkg : ApplicationRegister.getPgs()) {
            final Set<Class<?>> contexts = scanner.scan(pkg);
            for (Class<?> context : contexts) {
                final Jdbc jdbc = context.getAnnotation(Jdbc.class);
                if (jdbc != null) {
                    initBean(context);
                }
            }
        }
    }

    private void initBean(Class clazz) {
        final CgcgScanBeanFactory beanFactory = new CgcgScanBeanFactory();
        beanFactory.setClazz(clazz);
        beanFactory.setPkg(clazz.getPackage().getName());
        final JdbcFactoryBean factoryBean = new JdbcFactoryBean();
        factoryBean.setObjectType(clazz);
        beanFactory.setBean(factoryBean.getObject());
        ApplicationContext.putBean(beanFactory);
        final Object bean = beanFactory.getBean();
        if (bean != null) {
            final Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
            }
        }
    }

}
