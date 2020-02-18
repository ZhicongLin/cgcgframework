package org.cgcgframework.core.annotation;

import java.lang.annotation.*;

@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CBean {
}
