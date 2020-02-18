package com.cgcg.test;

import org.cgcgframework.web.HttpMethod;
import org.cgcgframework.web.annotation.CApi;
import org.cgcgframework.web.annotation.mapping.GET;
import org.cgcgframework.web.annotation.mapping.Mapping;
import org.cgcgframework.web.annotation.mapping.POST;
import org.cgcgframework.web.annotation.parameter.FormData;
import org.cgcgframework.web.annotation.parameter.Param;
import org.cgcgframework.web.annotation.parameter.ParamBody;
import org.cgcgframework.web.parameter.HeaderParameter;
import org.cgcgframework.web.parameter.ParamType;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@CApi
public class TestApi {

    @GET("/test")
    public String result(HeaderParameter header, @Param(value = "p1", type = ParamType.body) int params) {
        return "123我擦咧" + params;
    }

    @POST("/test")
    public TestForm POST(@FormData TestForm form) {
        return form;
    }

    @Mapping(value = "/test2", httpMethod = HttpMethod.POST)
    public Object result2(HttpServletRequest req, @ParamBody Map<String, Object> res) {
        return res;
    }
}
