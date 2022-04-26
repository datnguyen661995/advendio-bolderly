package com.advendio.marketplaceborderlyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class MarketplaceBorderlyServiceApplication {
//    @Autowired
//    JwtConfiguration jwtConfiguration;

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceBorderlyServiceApplication.class, args);
    }

//    @Bean
//    public ConfigurableJWTProcessor configurableJWTProcessor() throws MalformedURLException {
//        ResourceRetriever resourceRetriever =
//                new DefaultResourceRetriever(jwtConfiguration.getConnectionTimeout(),
//                        jwtConfiguration.getReadTimeout());
//        URL jwkSetURL = new URL(jwtConfiguration.getJwkUrl());
//        JWKSource keySource = new RemoteJWKSet(jwkSetURL, resourceRetriever);
//        ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
//        JWSKeySelector keySelector = new JWSVerificationKeySelector(RS256, keySource);
//        jwtProcessor.setJWSKeySelector(keySelector);
//        return jwtProcessor;
//    }
}
