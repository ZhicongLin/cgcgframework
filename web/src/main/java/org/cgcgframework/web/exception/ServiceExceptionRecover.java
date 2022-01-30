package org.cgcgframework.web.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: cgcgframework
 * @description: 系统异常处理器
 * @author: zhicong.lin
 * @create: 2022-01-30 17:48
 **/
public class ServiceExceptionRecover {

    public static void recover(ServiceException e, HttpServletResponse response) {
        final JSONObject jo = new JSONObject();
        jo.put("code", e.getCode());
        jo.put("message", e.getMessage());
        jo.put("success", false);
        try {
            response.getWriter().print(jo.toJSONString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
