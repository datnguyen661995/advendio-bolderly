/* (C)2022 */
package com.advendio.marketplace.openservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableFeignClients
// @EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class MarketplaceOpenServiceApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MarketplaceOpenServiceApplication.class, args);
    }
}
