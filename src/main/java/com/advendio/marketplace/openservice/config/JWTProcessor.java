/* (C)2022 */
package com.advendio.marketplace.openservice.config;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

import com.advendio.marketplace.openservice.authenticate.AwsCognitoIdTokenProcessor;
import com.advendio.marketplace.openservice.authenticate.AwsCognitoJwtAuthFilter;
import com.advendio.marketplace.openservice.properties.JwtProperties;
import com.advendio.marketplace.openservice.properties.OpenServiceIgnoringMatchers;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class JWTProcessor {
    @Autowired private JwtProperties jwtProperties;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired private AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor;

    @Autowired private OpenServiceIgnoringMatchers openServiceIgnoringMatchers;

    @Bean
    public ConfigurableJWTProcessor configurableJWTProcessor() throws MalformedURLException {
        ResourceRetriever resourceRetriever =
                new DefaultResourceRetriever(
                        jwtProperties.getConnectionTimeout(), jwtProperties.getReadTimeout());
        URL jwkSetURL = new URL(jwtProperties.getJwkUrl());
        JWKSource keySource = new RemoteJWKSet(jwkSetURL, resourceRetriever);
        ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
        JWSKeySelector keySelector = new JWSVerificationKeySelector(RS256, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);
        return jwtProcessor;
    }

    @Bean
    public AwsCognitoJwtAuthFilter awsCognitoJwtAuthFilter() {
        return new AwsCognitoJwtAuthFilter(
                awsCognitoIdTokenProcessor, openServiceIgnoringMatchers, resolver);
    }
}
