package com.example.moldMaker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/*
* *CORS(Cross-Origin Resource Sharing)**를 허용하면, 로컬 환경에서 **리액트 서버(예: http://localhost:3000)**와
* **백엔드 서버(예: http://localhost:8080)**가 서로 다른 포트를 사용하더라도 통신이 가능합니다.
* */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
