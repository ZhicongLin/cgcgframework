package org.cgcgframework.core.aop.annotation;

import java.lang.annotation.*;

/**
 * @author zhicong.lin
 */
@Target(value = ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface  Before {

    String value() default "";

    Class<?> anno() default Void.class;
}
