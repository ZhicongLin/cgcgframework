package org.cgcgframework.core.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public interface ApplicationPropertiesWare {

    default void load(Class<?> clazz, ApplicationProperties applicationProperties) {}

    default void load(Class<?> clazz, ApplicationProperties applicationProperties, String name) {
        final InputStream resourceAsStream = clazz.getClassLoader().getResourceAsStream(name);
        if (resourceAsStream == null) {
            return;
        }
        try {
            applicationProperties.getProperties().load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
