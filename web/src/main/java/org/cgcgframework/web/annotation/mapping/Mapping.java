package org.cgcgframework.web.annotation.mapping;

import org.cgcgframework.web.HttpMethod;

import java.lang.annotation.*;
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Mapping {
    String value() default "";
    HttpMethod httpMethod();
}
