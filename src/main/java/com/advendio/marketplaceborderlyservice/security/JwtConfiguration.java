package com.advendio.marketplaceborderlyservice.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "app.bolderly.jwt.aws")
public class JwtConfiguration {

    private String userPoolId;
    private String identityPoolId;
    private String jwkUrl;
    private String region;
    private String userNameField;
    private int connectionTimeout;
    private int readTimeout;
    private String httpHeader;

    public String getCognitoIdentityPoolUrl() {
        return String.format("https://cognito-idp.%s.amazonaws.com/%s", this.region, this.userPoolId);
    }

    public String getJwkUrl() {
        return this.jwkUrl != null && !this.jwkUrl.isEmpty() ? this.jwkUrl : String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", this.region, this.userPoolId);
    }
}
