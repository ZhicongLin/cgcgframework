package org.cgcgframework.core.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
@Slf4j
public class MethodAopInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("Before: interceptor name: {}", invocation.getMethod().getName());

        log.info("Arguments: {}", JSON.toJSONString(invocation.getArguments()));

        Object result = invocation.proceed();

        log.info("After: result: {}", JSON.toJSONString(result));
        return result;
    }
}
