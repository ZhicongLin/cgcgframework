package org.cgcgframework.core;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.cgcgframework.core.proxy.BeanProxy;

/**
 * @author zhicong.lin
 */
@Slf4j
@Setter
@Getter
public class CgcgScanBeanFactory {

    private String pkg;

    private Class<?> clazz;

    private Object bean;

    public CgcgScanBeanFactory( Class<?> clazz) {
        this.pkg = clazz.getPackage().getName();
        this.clazz = clazz;
        this.initBean();
    }

    public CgcgScanBeanFactory() {
    }

    private void initBean() {
        if (this.clazz.isAnnotation() || this.clazz.isInterface()) {
            return;
        }
        this.bean = new BeanProxy().getProxy(clazz);
    }
}
