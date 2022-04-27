package com.advendio.marketplaceborderlyservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.bolderly.key")
public class KeyProperties {
    private String publicKey;
    private String privateKey;
}
