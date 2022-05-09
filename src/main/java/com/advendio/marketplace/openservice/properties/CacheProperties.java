/* (C)2022 */
package com.advendio.marketplace.openservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.service.cache")
public class CacheProperties {
    private int maximumSize;
    private int initialCapacity;
    private CacheExpire mins;
    private Boolean enabled;

    @Getter
    @Setter
    public static class CacheExpire {
        private int expireAfterWrite;
    }
}
