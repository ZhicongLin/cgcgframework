package org.cgcgframework.core.aop.annotation;

import org.cgcgframework.core.annotation.CBean;

import java.lang.annotation.*;

/**
 * @author zhicong.lin
 */
@Target(value = ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@CBean
public @interface Aop {
}
