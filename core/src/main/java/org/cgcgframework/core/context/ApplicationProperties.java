package org.cgcgframework.core.context;

import lombok.Getter;
import lombok.Setter;
import org.cgcgframework.core.annotation.CBean;

import java.util.Properties;

@Setter
@Getter
@CBean
public class ApplicationProperties {

    private Properties properties = new Properties();

    public Integer getInteger(String key) {
        return Integer.valueOf(properties.getProperty(key));
    }

    public Integer getLong(String key) {
        return Integer.valueOf(properties.getProperty(key));
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public <T> T get(String key, Class<T> tClass) {
        final boolean hasKey = properties.containsKey(key);
        if (Integer.class.equals(tClass)) {
            return hasKey ? (T) getInteger(key) : null;
        } else if (Long.class.equals(tClass)) {
            return hasKey ? (T) getLong(key) : null;
        }
        return hasKey ? (T) getProperty(key) : null;
    }
}
