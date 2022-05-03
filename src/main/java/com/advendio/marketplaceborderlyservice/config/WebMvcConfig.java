package com.advendio.marketplaceborderlyservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final BolderlyHttpInterceptor bolderlyHttpInterceptor;

    public WebMvcConfig(BolderlyHttpInterceptor bolderlyHttpInterceptor) {
        this.bolderlyHttpInterceptor = bolderlyHttpInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bolderlyHttpInterceptor);
    }
}
