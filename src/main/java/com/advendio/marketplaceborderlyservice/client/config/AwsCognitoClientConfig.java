package com.advendio.marketplaceborderlyservice.client.config;

import com.advendio.marketplaceborderlyservice.properties.JwtProperties;
import feign.auth.BasicAuthRequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;

@AllArgsConstructor
public class AwsCognitoClientConfig {
    private final JwtProperties jwtProperties;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(jwtProperties.getClientId(), jwtProperties.getClientSecret());
    }

}
