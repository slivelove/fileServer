package com.server.fileServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import utils.FileTools;

import javax.servlet.MultipartConfigElement;
import java.util.Properties;

@SpringBootApplication
@EnableScheduling
public class FileServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class, args);
    }

    /**
     * 文件上传配置
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        Properties p = FileTools.getProperties();
        //单个文件最大
        factory.setMaxFileSize(p.getProperty("maxFileSize"));
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(p.getProperty("maxRequestSize"));
        return factory.createMultipartConfig();
    }
}
