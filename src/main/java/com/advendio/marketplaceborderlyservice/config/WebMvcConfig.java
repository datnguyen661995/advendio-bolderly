/* (C)2022 */
package com.advendio.marketplaceborderlyservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final HttpLoggingInterceptor httpLoggingInterceptor;

    public WebMvcConfig(HttpLoggingInterceptor httpLoggingInterceptor) {
        this.httpLoggingInterceptor = httpLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpLoggingInterceptor);
    }
}
