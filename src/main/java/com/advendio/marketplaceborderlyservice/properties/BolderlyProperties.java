/* (C)2022 */
package com.advendio.marketplaceborderlyservice.properties;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.bolderly")
public class BolderlyProperties {
    private List<AntPathRequestMatcher> ignoringMatchers;
    private String privateKey;
}
