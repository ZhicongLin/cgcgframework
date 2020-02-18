package org.cgcgframework.web;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.servlet.api.ServletInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.annotation.CValue;
import org.cgcgframework.core.context.ApplicationProperties;
import org.cgcgframework.core.context.ApplicationPropertiesWare;
import org.cgcgframework.core.context.Application;
import org.cgcgframework.core.context.ApplicationInitializationWare;

import javax.servlet.ServletException;
import java.io.IOException;

@Slf4j
@CBean
public class UndertowService implements ApplicationInitializationWare, ApplicationPropertiesWare {

    @CValue("cgcg.port")
    private Integer port;
    @CValue("cgcg.host")
    private String host;
    @CValue("cgcg.context-path")
    private String contextPath;
    @CValue("cgcg.application.name")
    private String name;

    @Override
    public void load(Class<?> clazz, ApplicationProperties applicationProperties) {
        load(clazz, applicationProperties, "cgcg.properties");
    }

    private void start(Class<?> clazz) throws IOException {
        final long start = System.currentTimeMillis();
        /*
         * 创建ServletInfo，Servelt的最小单位。是对javax.servlet.Servlet具体实现的再次封装。
         * 注意：ServletInfo的name必须是唯一的
         */
        ServletInfo servletInfo = Servlets.servlet("MyServlet", DispatcherServlet.class);
        // 创建servletInfo的初始化参数
        servletInfo.addInitParam("message", "This is my first MyServlet!");
        // 绑定映射为/myServlet
        servletInfo.addMapping("/");

        String path = contextPath;
        if (StringUtils.isBlank(contextPath)) {
            path = "/";
        }
        /**
         * 创建包部署对象，包含多个servletInfo。可以认为是servletInfo的集合
         */
        DeploymentInfo deploymentInfo = Servlets.deployment();
        // 指定ClassLoader
        deploymentInfo.setClassLoader(clazz.getClassLoader());
        // 应用上下文(必须与映射路径一致，否则sessionId会出现问题，每次都会新建)
        deploymentInfo.setContextPath(path);
        // 设置部署包名
        deploymentInfo.setDeploymentName(name);
        // 添加servletInfo到部署对象中
        deploymentInfo.addServlets(servletInfo);
        /**
         * 使用默认的servlet容器，并将部署添加至容器
         * 容器，用来管理DeploymentInfo，一个容器可以添加多个DeploymentInfo
         */
        ServletContainer container = Servlets.defaultContainer();
        /**
         * 将部署添加至容器并生成对应的容器管理对象
         * 包部署管理。是对添加到ServletContaint中DeploymentInfo的一个引用，用于运行发布和启动容器
         */
        DeploymentManager manager = container.addDeployment(deploymentInfo);
        // 实施部署
        manager.deploy();
        /**
         * 分发器：将用户请求分发给对应的HttpHandler
         */
        PathHandler pathHandler = Handlers.path();
        /**
         * servlet path处理器，DeploymentManager启动后返回的Servlet处理器。
         */
        HttpHandler myApp = null;
        try {
            //启动容器，生成请求处理器
            myApp = manager.start();
        } catch (ServletException e) {
            throw new RuntimeException("容器启动失败！");
        }
        //绑定映射关系
        pathHandler.addPrefixPath(path, myApp);

        Undertow server = Undertow.builder()
                //绑定端口号和主机
                .addHttpListener(port, host)
                //设置分发处理器
                .setHandler(pathHandler).build();
        log.info("Starting Service [Undertow]");
        log.info("Starting Servlet engine: [Undertow 2.0.28.Final]");
        //启动server
        server.start();

        Application.serverTime =  System.currentTimeMillis() - start;
        log.info("Undertow started on port(s): {} (http) with context path '{}'.", port, path);
    }

    public void initialization(Class<?> clazz) {
        try {
            this.start(clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
