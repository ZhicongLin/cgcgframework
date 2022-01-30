package org.cgcgframework.core.context;

public interface ApplicationInitializationWare {

    /**
     * 容器初始化后加载
     *
     * @author : zhicong.lin
     * @date : 2022/1/30 15:53
     */
    default void initialization() {
    }

    /**
     * 容器初始化后加载
     *
     * @param clazz
     * @author : zhicong.lin
     * @date : 2022/1/30 15:54
     */
    default void initialization(Class<?> clazz) {
    }
}
