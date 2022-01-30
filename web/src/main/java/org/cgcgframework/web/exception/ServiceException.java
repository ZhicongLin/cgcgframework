package org.cgcgframework.web.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: cgcgframework
 * @description: 系统异常
 * @author: zhicong.lin
 * @create: 2022-01-30 17:46
 **/
@Setter
@Getter
public class ServiceException extends RuntimeException {

    private int code;

    private String message;

    public ServiceException(int code, String message) {
        super(message);
    }

    public ServiceException(String message) {
        super(message);
        this.code = 400;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
