package org.cgcgframework.core.event;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EventProcessor {

    Class<?> value() default Void.class;
}
