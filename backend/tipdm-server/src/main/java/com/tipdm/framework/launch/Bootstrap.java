package com.tipdm.framework.launch;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhoulong on 2017/11/5.
 * E-mail:zhoulong8513@gmail.com
 */
@SuppressWarnings("all")
public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) throws Exception {

        System.setProperty("tomcat.util.scan.StandardJarScanFilter.jarsToSkip", "*.jar");
        //从jvm启动参数获取端口，上下文，项目根路径
        //端口号
        int port =Integer.parseInt(System.getProperty("server.port", "8088"));
        //上下文
        String contextPath = System.getProperty("server.contextPath", "/dmserver");
        //根路径
        String docBase = System.getProperty("server.docBase", ServerConfig.getWebRoot());
        logger.debug("Tomcat will be starting on port : {}, context path : {},doc base : {}",port, contextPath, docBase);
        Tomcat tomcat = ServerConfig.createTomcat(port,contextPath, docBase);
        tomcat.start();

        //添加钩子函数
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run(){
                try {
                    tomcat.stop();
                } catch (LifecycleException e) {
                    logger.error("stoptomcat error.", e);
                }
            }
        });

        tomcat.getServer().await();
    }

}
