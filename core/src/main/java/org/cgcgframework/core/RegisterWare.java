package org.cgcgframework.core;

/**
 * @author zhicong.lin
 */
public interface RegisterWare {

    /**
     * 注册方法
     */
    default void register() {
    }

    /**
     * 注册方法
     * @param scanner
     * @return void
     * @author : zhicong.lin
     * @date : 2022/1/30 15:51
     */
    default void register(CgcgScanner scanner) {
        register();
    }
}
