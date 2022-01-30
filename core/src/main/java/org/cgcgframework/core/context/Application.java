package org.cgcgframework.core.context;

import lombok.extern.slf4j.Slf4j;
import org.cgcgframework.core.ApplicationRegister;
import org.cgcgframework.core.annotation.CInit;
import org.cgcgframework.core.annotation.CScan;
import org.cgcgframework.core.annotation.CStarter;
import org.cgcgframework.core.event.EventContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class Application {
    private static final long currentTime = System.currentTimeMillis();
    public static long serverTime = 0;

    public static void run(Class<?> clazz, String... args) {
        //获取扫描包
        final Set<String> packages = getScanPackage(clazz);
        //注册javaBean
        ApplicationRegister.register(packages, clazz);
        //执行系统初始化
        final List<ApplicationInitializationWare> wares = ApplicationContext.getInitializations();
        for (ApplicationInitializationWare ware : wares) {
            ware.initialization();
        }
        //加载事件监听器
        EventContext.initListener();
        log.info("Started {} in {} seconds. (JVM running for {})", clazz.getSimpleName(), (serverTime) / 1000.d, (System.currentTimeMillis() - currentTime) / 1000.d);
        //执行初始化的@CInit
        initializationApplicationContext();
    }

    private static Set<String> getScanPackage(Class<?> clazz) {
        final Set<String> packages = new HashSet<>();
        packages.add(clazz.getPackage().getName());
        final CStarter cStarter = clazz.getDeclaredAnnotation(CStarter.class);
        CScan cScan = clazz.getAnnotation(CScan.class);
        if (cScan == null) {
            final Class<? extends Annotation> annoType = cStarter.annotationType();
            cScan = annoType.getAnnotation(CScan.class);
        }
        packages.addAll(Arrays.asList(cScan.value()));
        return packages;
    }

    private static void initializationApplicationContext() {
        final Map<Class<?>, Object> context = ApplicationContext.getContext();
        final Set<Class<?>> classSet = context.keySet();
        // 获取全部注解Cinit的方法
        final List<Initialization> initializations = new ArrayList<>();
        classSet.forEach(aClass -> {
            final Method[] methods = aClass.getDeclaredMethods();
            Arrays.stream(methods).forEach(method -> {
                final CInit cInit = method.getDeclaredAnnotation(CInit.class);
                if (cInit != null) {
                    initializations.add(new Initialization(context.get(aClass), method, cInit.order()));
                }
            });
        });

        // 根据获取初始化的方法上面的order进行排序
        initializations.sort(Comparator.comparing(Initialization::getOrder));

        //执行初始化方法
        initializations.forEach(init -> {
            try {
                init.getMethod().invoke(init.getBean());
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

}
