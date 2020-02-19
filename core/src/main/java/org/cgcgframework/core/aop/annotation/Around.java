package org.cgcgframework.core.aop.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Around {

    String value() default "";
    Class<?> anno() default Void.class;
}
