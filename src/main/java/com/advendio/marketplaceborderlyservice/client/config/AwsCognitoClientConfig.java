package com.advendio.marketplaceborderlyservice.client.config;

import com.advendio.marketplaceborderlyservice.properties.JwtProperties;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@AllArgsConstructor
public class AwsCognitoClientConfig {
    private final JwtProperties jwtProperties;
    private final ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(jwtProperties.getClientId(), jwtProperties.getClientSecret());
    }

//    @Bean
//    @Primary
//    @Scope(SCOPE_PROTOTYPE)
//    Encoder feignFormEncoder() {
//        return new FormEncoder(new SpringEncoder(this.messageConverters));
//    }
}
