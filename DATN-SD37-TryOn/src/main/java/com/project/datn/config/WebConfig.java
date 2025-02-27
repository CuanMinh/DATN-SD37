package com.project.datn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectDir = null;
        try {
            projectDir = new ClassPathResource("").getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String uploadDir = Paths.get(projectDir, "../uploads/sanpham").normalize().toString();
        registry.addResourceHandler("/uploads/sanpham/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}