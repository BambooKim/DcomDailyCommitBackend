package com.bamboo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:server.properties")
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    Environment env;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
//        registry.addMapping("/**")
//                .allowedOrigins(env.getProperty("originUrl"))
//                .allowCredentials(true);
    }
}
