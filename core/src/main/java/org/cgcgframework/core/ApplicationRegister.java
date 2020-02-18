package org.cgcgframework.core;

import lombok.extern.slf4j.Slf4j;
import org.cgcgframework.core.context.ApplicationContext;
import org.cgcgframework.core.context.ApplicationProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ApplicationRegister {

    private static CgcgScanner cgcgScanner = new CgcgScanner();
    private static Set<String> pkgs = new HashSet<>();

    public static synchronized void register(Set<String> pkgs, Class<?> clazz) {
        ApplicationRegister.pkgs = pkgs;
        ApplicationRegister.initProperties(clazz.getClassLoader());
        //加载注册器
        for (String pkg : pkgs) {
            final Set<Class<?>> contexts = cgcgScanner.scan(pkg);
            for (Class<?> context : contexts) {
                ApplicationRegister.loadRegister(context);
            }
        }
        //执行注册器
        final List<ApplicationContext.RegisterHandle> registers = ApplicationContext.getRegisters();
        registers.sort((o1, o2) -> o1.getOrder() > o2.getOrder() ? 1 : 0);
        for (ApplicationContext.RegisterHandle register : registers) {
            register.getRegister().register();
        }

        //注入依赖的Bean
        ApplicationContext.injection();
    }

    /**
     * 初始化配置文件
     * @param classLoader classLoader
     */
    private static void initProperties(ClassLoader classLoader) {
        final ApplicationProperties applicationProperties = ApplicationContext.getBean(ApplicationProperties.class);
        final InputStream stream = classLoader.getResourceAsStream("application.properties");
        if (stream != null) {
            try {
                applicationProperties.getProperties().load(stream);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private static void loadRegister(Class<?> clazz) {
        if (!RegisterWare.class.isAssignableFrom(clazz) || clazz.isInterface()) {
            return ;
        }
        try {
            final CgcgScanBeanFactory beanFactory = new CgcgScanBeanFactory();
            final Object bean = clazz.newInstance();
            beanFactory.setBean(bean);
            beanFactory.setClazz(clazz);
            beanFactory.setPkg(clazz.getPackage().getName());
            ApplicationContext.putBean(beanFactory);
            log.info("Load Register [{}]", clazz);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static Set<String> getPkgs() {
        return pkgs;
    }
}
