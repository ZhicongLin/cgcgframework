package org.cgcgframework.core.annotation;

import java.lang.annotation.*;

/**
 * 容器初始化时，执行的注解
 * @author zhicong.lin
 */
@Target(value = ElementType.METHOD)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CInit {

    int order() default 0;
}
