package org.cgcgframework.core.event;

/**
 * 返回值类型不匹配异常
 */
public class CastResultTypeException extends RuntimeException {

    public CastResultTypeException(Class<?> resultClass, Class<?> needClass) {
        super(String.format("返回值类型不匹配异常, 需要的是%s, 而实际得到的是%s", needClass.getName(), resultClass.getName()));
    }

}
