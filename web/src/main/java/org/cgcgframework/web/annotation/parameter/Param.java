package org.cgcgframework.web.annotation.parameter;

import org.cgcgframework.web.parameter.ParamType;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

    String value();

    ParamType type() default ParamType.query;
}
