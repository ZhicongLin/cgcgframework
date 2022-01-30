package org.cgcgframework.core.annotation;

import java.lang.annotation.*;

/**
 * @author zhicong.lin
 */
@Target(value = ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@CScan
@CBean
public @interface Application {
}
