/* (C)2022 */
package com.advendio.marketplace.openservice.properties;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.service")
public class OpenServiceIgnoringMatchers {
    private List<AntPathRequestMatcher> ignoringMatchers;
}
