package com.advancedMobileProgramming.global.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebStaticConfig implements WebMvcConfigurer {
    @Value("${storage.local.base-dir:uploads}")
    private String baseDir;

    private String absoluteBase() {
        Path base = Paths.get(baseDir);
        if (!base.isAbsolute()) {
            base = Paths.get(System.getProperty("user.home")).resolve(baseDir);
        }
        return base.toAbsolutePath().normalize().toString();
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:" + absoluteBase() + "/");
    }

}