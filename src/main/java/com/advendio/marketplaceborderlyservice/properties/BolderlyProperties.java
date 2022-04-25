package com.advendio.marketplaceborderlyservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.bolderly")
public class BolderlyProperties {
    private List<AntPathRequestMatcher> ignoringMatchers;
}
