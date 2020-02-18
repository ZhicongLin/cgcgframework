package org.cgcgframework.web;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.cgcgframework.web.parameter.FileTempContext;
import org.cgcgframework.web.parameter.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

@Slf4j
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("json/application; charset=utf-8");
        final String uri = request.getRequestURI().replaceFirst(request.getContextPath(), "");
        final Method method = CgcgServletContext.getMethod(uri, HttpMethod.valueOf(request.getMethod()));
        if (method == null) {
            response.getWriter().print("资源路径不存在");
            return;
        }
        final Logger logger = LoggerFactory.getLogger(method.getDeclaringClass() + "." + method.getName());
        logger.info("{} {}", request.getMethod(), request.getRequestURL());
        final long start = System.currentTimeMillis();
        try {
            final Object[] args = ParameterResolver.getArgs(method, request, response);
            if (args != null) {
                for (Object arg : args) {
                    if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                        continue;
                    }
                    logger.debug("{}", JSON.toJSONString(arg));
                }
            }
            final Object invoke = method.invoke(CgcgServletContext.getBean(method), args);
            response.getWriter().print(JSON.toJSON(invoke));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            logger.info("End {} Time {}ms.", request.getRequestURL(), System.currentTimeMillis() - start);
            response.getWriter().close();
            final Set<String> filePathSet = FileTempContext.get();
            if (filePathSet != null) {
                for (String filePath : filePathSet) {
                    try {
                        FileUtils.forceDelete(new File(filePath));
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                FileTempContext.clear();
            }
        }
    }



}
