package com.itranswarp.summer.boot;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;

import org.apache.catalina.Context;
import org.apache.catalina.Server;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itranswarp.summer.io.PropertyResolver;
import com.itranswarp.summer.utils.ClassPathUtils;
import com.itranswarp.summer.web.ContextLoaderInitializer;
import com.itranswarp.summer.web.utils.WebUtils;

public class SummerApplication {

    static final String CONFIG_APP_YAML = "/application.yml";
    static final String CONFIG_APP_PROP = "/application.properties";

    final Logger logger = LoggerFactory.getLogger(SummerApplication.class);

    //根据反射来获得进程的pid，Java8没有getpid()，java9引进
    public static long getPid() {

        try {
            // 尝试在 Java 9+ 上调用 getPid()
            Method getPidMethod = ManagementFactory.class.getMethod("getPid"
            );
            return
                    (Long) getPidMethod.invoke(ManagementFactory.getRuntimeMXBean());
        } catch
        (Exception e) {
            // 在 Java 8 环境下，通过进程名获取 PID
            String name =
                    ManagementFactory.getRuntimeMXBean().getName();
            return Long.parseLong(name.split("@")[0
                    ]);
        }
    }


    public static void run(String webDir, String baseDir, Class<?> configClass, String... args) throws Exception {
        new SummerApplication().start(webDir, baseDir, configClass, args);
    }

    public void start(String webDir, String baseDir, Class<?> configClass, String... args) throws Exception {
        printBanner();

        // start info:
        final long startTime = System.currentTimeMillis();
        //final int javaVersion = Runtime.version().feature();
        //final long pid = ManagementFactory.getRuntimeMXBean().getPid();
        //获取版本号，或许可以直接final int javaVersion = 手动键入版本号;？
        String sjavaVersion = System.getProperty("java.version");

        // 提取主版本号（例如：从 "1.8.0_251" 中提取 "8"）
        int majorVersion = Integer.parseInt(sjavaVersion.split("\\.")[1]);  // "1.8.0" -> "8"

        // 使用 final int 类型
        final int javaVersion = majorVersion;


        final long pid = getPid();
        final String user = System.getProperty("user.name");
        final String pwd = Paths.get("").toAbsolutePath().toString();
        logger.info("Starting {} using Java {} with PID {} (started by {} in {})", configClass.getSimpleName(), javaVersion, pid, user, pwd);

        var propertyResolver = WebUtils.createPropertyResolver();
        var server = startTomcat(webDir, baseDir, configClass, propertyResolver);

        // started info:
        final long endTime = System.currentTimeMillis();
        final String appTime = String.format("%.3f", (endTime - startTime) / 1000.0);
        final String jvmTime = String.format("%.3f", ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0);
        logger.info("Started {} in {} seconds (process running for {})", configClass.getSimpleName(), appTime, jvmTime);

        server.await();
    }

    //    protected Server startTomcat(String webDir, String baseDir, Class<?> configClass, PropertyResolver propertyResolver) throws Exception {
//        int port = propertyResolver.getProperty("${server.port:8080}", int.class);
//        logger.info("starting Tomcat at port {}...", port);
//        Tomcat tomcat = new Tomcat();
//        tomcat.setPort(port);
//        tomcat.getConnector().setThrowOnFailure(true);
//        Context ctx = tomcat.addWebapp("", new File(webDir).getAbsolutePath());
//        WebResourceRoot resources = new StandardRoot(ctx);
//        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", new File(baseDir).getAbsolutePath(), "/"));
//        ctx.setResources(resources);
//        ctx.addServletContainerInitializer(new ContextLoaderInitializer(configClass, propertyResolver), Set.of());
//        tomcat.start();
//        logger.info("Tomcat started at port {}...", port);
//        return tomcat.getServer();
//    }
    protected Server startTomcat(String webDir, String baseDir, Class<?> configClass, PropertyResolver propertyResolver) throws
            Exception {
        // 从配置中获取端口，默认8080
        int port = propertyResolver.getProperty("${server.port:8080}", int
                .class);
        logger.info(
                "Starting Tomcat at port {}..."
                , port);

        // 初始化 Tomcat 实例
        Tomcat tomcat = new Tomcat
                ();
        tomcat.setPort(port);
        tomcat.getConnector().setThrowOnFailure(
                true); // 确保连接失败时抛出异常

        // 配置 Web 应用的上下文
        Context ctx = tomcat.addWebapp("", new File
                (webDir).getAbsolutePath());

        // 配置 Web 资源目录
        WebResourceRoot resources = new StandardRoot
                (ctx);
        File baseDirFile = new File(baseDir).getAbsoluteFile(); // 获取绝对路径
        resources.addPreResources(
                new DirResourceSet(resources, "/WEB-INF/classes", baseDirFile.getAbsolutePath(), "/"
                ));

        // 将资源目录绑定到上下文
        ctx.setResources(resources);

        // 添加 Servlet 容器初始化器，初始化 Spring 上下文
        ctx.addServletContainerInitializer(
                new ContextLoaderInitializer
                        (configClass, propertyResolver), Collections.emptySet());

        // 启动 Tomcat 服务
        tomcat.start();
        logger.info(
                "Tomcat started at port {}..."
                , port);

        // 返回 Tomcat 的服务器实例
        return
                tomcat.getServer();
    }
//    protected void printBanner() {
//        String banner = ClassPathUtils.readString("/banner.txt");
//        banner.lines().forEach(System.out::println);
//    }
//}

    protected void printBanner() {
        String banner = ClassPathUtils.readString("/banner.txt");

        // 使用 split("\n") 按行拆分字符串
        String[] lines = banner.split("\n");

        // 遍历每一行并打印
        for (String line : lines) {
            System.out.println(line);
        }
    }

}