package com.cgcg.test;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAopAnno {
}
