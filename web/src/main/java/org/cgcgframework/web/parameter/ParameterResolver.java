package org.cgcgframework.web.parameter;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.cgcgframework.web.annotation.parameter.Header;
import org.cgcgframework.web.annotation.parameter.Param;
import org.cgcgframework.web.annotation.parameter.ParamBody;
import org.cgcgframework.web.annotation.parameter.FormData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ParameterResolver {

    /**
     * 获取指定方法的参数名
     *
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表
     */
    public static String[] getMethodParameterNamesByAnnotation(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return null;
        }
        String[] parameterNames = new String[parameterAnnotations.length];
        int i = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof Param) {
                    Param param = (Param) annotation;
                    parameterNames[i++] = param.value();
                }
            }
        }
        return parameterNames;
    }

    public static Object[] getArgs(Method method, HttpServletRequest request, HttpServletResponse response) {
        Object[] params;
        final Parameter[] parameters = method.getParameters();
        if (parameters == null) {
            return null;
        } else if (parameters.length == 0) {
            return new Object[]{};
        } else {
            params = new Object[parameters.length];
        }
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            final Class<?> type = parameter.getType();
            if (type.equals(HttpServletRequest.class)) {
                params[i] = request;
            } else if (type.equals(HttpServletResponse.class)) {
                params[i] = response;
            } else if (type.equals(HeaderParameter.class)) {
                params[i] = new HeaderParameter(request);
            } else {
                final Header header = parameter.getAnnotation(Header.class);
                if (header != null) {
                    params[i] = paramType(request.getParameter(header.value()), parameter.getType());
                }
                final Param param = parameter.getAnnotation(Param.class);
                if (param != null) {
                    params[i] = paramType(request.getParameter(param.value()), parameter.getType());
                }
                final ParamBody paramBody = parameter.getAnnotation(ParamBody.class);
                if (paramBody != null) {
                    final String body = HttpServletRequestReader.readAsChars(request);
                    if (StringUtils.isNotBlank(body)) {
                        params[i] = JSON.parseObject(body, parameter.getType());
                    }
                }
                final FormData formData = parameter.getAnnotation(FormData.class);
                if (formData != null) {
                    params[i] = HttpServletRequestReader.readFormData(request, parameter.getType());
                }
            }
        }

        return params;
    }

    @SuppressWarnings("unchecked")
    private static <T> T paramType(String param, Class<T> tClass) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        if (tClass.equals(Integer.class) || tClass.equals(int.class)) {
            return (T) Integer.valueOf(param);
        } else if (tClass.equals(Double.class) || tClass.equals(double.class)) {
            return (T) Double.valueOf(param);
        } else if (tClass.equals(Float.class) || tClass.equals(float.class)) {
            return (T) Float.valueOf(param);
        } else if (tClass.equals(short.class) || tClass.equals(Short.class)) {
            return (T) Short.valueOf(param);
        } else if (tClass.equals(Byte.class) || tClass.equals(byte.class)) {
            return (T) Byte.valueOf(param);
        } else if (tClass.equals(Boolean.class) || tClass.equals(boolean.class)) {
            return (T) Boolean.valueOf(param);
        } else if (tClass.equals(Long.class) || tClass.equals(long.class)) {
            return (T) Long.valueOf(param);
        }
        return (T) param;
    }

}
