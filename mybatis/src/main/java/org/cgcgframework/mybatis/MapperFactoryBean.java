package org.cgcgframework.mybatis;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;

public class MapperFactoryBean<T> extends SqlSessionDaoSupport {
    private Class<T> mapperInterface;
    private boolean addToConfig = true;

    public MapperFactoryBean() {
    }

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    protected void checkDaoConfig() {
        Configuration configuration = this.getSqlSession().getConfiguration();
        if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
            try {
                configuration.addMapper(this.mapperInterface);
            } catch (Exception var6) {
                throw new IllegalArgumentException(var6);
            } finally {
                ErrorContext.instance().reset();
            }
        }

    }

    public T getObject() throws Exception {
        return this.getSqlSession().getMapper(this.mapperInterface);
    }

    public Class<T> getObjectType() {
        return this.mapperInterface;
    }
}