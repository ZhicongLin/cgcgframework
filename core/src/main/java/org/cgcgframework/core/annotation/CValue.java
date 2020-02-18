package org.cgcgframework.core.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.FIELD)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CValue {

    String value() default "";
}
