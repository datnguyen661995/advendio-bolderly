/* (C)2022 */
package com.advendio.marketplace.openservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ForkJoinPoolFactoryBean;

@Configuration
public class ThreadConfig {

    @Value("${config.threadpool.corepool.size}")
    private int corePoolSize;

    @Bean
    public ForkJoinPoolFactoryBean forkJoinPoolFactoryBean() {
        final ForkJoinPoolFactoryBean poolFactory = new ForkJoinPoolFactoryBean();
        poolFactory.setParallelism(corePoolSize);
        return poolFactory;
    }
}
