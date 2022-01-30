package org.cgcgframework.core.annotation;

import java.lang.annotation.*;

/**
 * @author zhicong.lin
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CBean {
}
