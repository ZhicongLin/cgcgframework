package org.cgcgframework.core.event;

import java.lang.annotation.*;

/**
 * @author zhicong.lin
 */
@Target(value = ElementType.METHOD)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Async {

}
