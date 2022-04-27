package com.advendio.marketplaceborderlyservice;

import com.advendio.marketplaceborderlyservice.utils.GenerateKeys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableFeignClients
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class MarketplaceBorderlyServiceApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MarketplaceBorderlyServiceApplication.class, args);
    }
}
