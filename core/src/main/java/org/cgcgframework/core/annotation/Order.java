package org.cgcgframework.core.annotation;

import java.lang.annotation.*;

/**
 * @author zhicong.lin
 */
@Target(value = ElementType.TYPE)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Order {

    int value() default 0;
}
