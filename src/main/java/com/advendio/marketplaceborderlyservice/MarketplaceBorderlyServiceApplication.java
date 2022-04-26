package com.advendio.marketplaceborderlyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableFeignClients
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class MarketplaceBorderlyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketplaceBorderlyServiceApplication.class, args);
    }
}
