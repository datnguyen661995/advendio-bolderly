/* (C)2022 */
package com.advendio.marketplaceborderlyservice.client.config;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import feign.codec.Encoder;
import feign.form.FormEncoder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
public class FormUrlConfig {
    private final ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    @Primary
    @Scope(SCOPE_PROTOTYPE)
    Encoder feignFormEncoder() {
        return new FormEncoder(new SpringEncoder(this.messageConverters));
    }
}
