package com.tipdm.framework.launch;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.JreMemoryLeakPreventionListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by zhoulong on 2017/11/7.
 * E-mail:zhoulong8513@gmail.com
 */
public final class ServerConfig {

    private final static Logger logger = LoggerFactory.getLogger(ServerConfig.class);

    protected static File getBaseDir(){
        File classpathDir = new File(Thread.currentThread().getContextClassLoader().getResource(".").getFile());
        File projectDir = classpathDir.getParentFile().getParentFile();
        logger.info("docBase: {}", projectDir.getAbsoluteFile());
        return  projectDir;
    }

    protected static String getWebRoot() {
        File webRoot = new File(getBaseDir(),"src/main/webapp");
        return webRoot.getAbsolutePath();
    }

    protected static Tomcat createTomcat(int port, String contextPath, String docBase) throws Exception{
        String tmpdir = System.getProperty("java.io.tmpdir");
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(tmpdir);
        tomcat.getHost().setAppBase(tmpdir);
        tomcat.getHost().setAutoDeploy(false);
        tomcat.getHost().setDeployOnStartup(false);
        tomcat.getEngine().setBackgroundProcessorDelay(-1);
        tomcat.setPort(port);
        StandardContext ctx = (StandardContext)tomcat.addWebapp(contextPath, docBase);

        StandardServer server =(StandardServer) tomcat.getServer();
        server.addLifecycleListener(new AprLifecycleListener());
        server.addLifecycleListener(new JreMemoryLeakPreventionListener());
        return tomcat;
    }

}
