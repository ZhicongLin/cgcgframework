package org.cgcgframework.web.annotation.parameter;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface FormData {

    String value() default "";

}
