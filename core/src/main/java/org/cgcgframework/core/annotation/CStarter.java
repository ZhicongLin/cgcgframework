package org.cgcgframework.core.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@CScan
@CBean
public @interface CStarter {
}
