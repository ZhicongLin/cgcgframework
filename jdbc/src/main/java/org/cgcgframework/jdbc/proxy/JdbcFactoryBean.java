package org.cgcgframework.jdbc.proxy;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JdbcFactoryBean {
    private Class objectType;
    public Object getObject() {
        return Proceeding.jdk(objectType);
    }

}