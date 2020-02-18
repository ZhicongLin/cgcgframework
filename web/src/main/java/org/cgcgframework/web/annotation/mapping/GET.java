package org.cgcgframework.web.annotation.mapping;

import org.cgcgframework.web.HttpMethod;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Mapping(httpMethod = HttpMethod.GET)
public @interface GET {

    String value();
}
