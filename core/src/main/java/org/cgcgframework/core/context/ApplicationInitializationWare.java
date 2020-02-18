package org.cgcgframework.core.context;

public interface ApplicationInitializationWare {

    default void initialization() {
    }
    default void initialization(Class<?> clazz) {
    }
}
