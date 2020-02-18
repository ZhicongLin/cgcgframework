package org.cgcgframework.core.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Order {

    int value() default 0;
}
