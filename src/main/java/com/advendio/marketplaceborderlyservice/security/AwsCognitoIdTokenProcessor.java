package com.advendio.marketplaceborderlyservice.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class AwsCognitoIdTokenProcessor {
    private static final String Bearer_ = "Bearer ";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final JwtConfiguration jwtConfiguration;
    private final ConfigurableJWTProcessor configurableJWTProcessor;

    public Authentication authenticate(HttpServletRequest request) throws Exception {
        String idToken = request.getHeader(this.jwtConfiguration.getHttpHeader());
        JWTClaimsSet claims = null;
        if (idToken != null) {
            claims = this.configurableJWTProcessor.process(this.getBearerToken(idToken), null);
        }
        validateIssuer(claims);
        verityIfIdToken(claims);
        String userName = getUserNameFrom(claims);
        if (userName != null) {
            List<GrantedAuthority> grantedAuthorities = Arrays.asList(new SimpleGrantedAuthority(ROLE_ADMIN));
            User user = new User(userName, "", new ArrayList<>());
            return new JwtAuthentication(user, claims, grantedAuthorities);
        }
        return null;
    }

    private String getBearerToken(String token) {
        return token.startsWith(Bearer_) ? token.substring(Bearer_.length()) : token;
    }

    private String getUserNameFrom(JWTClaimsSet claims) {
        return claims.getClaims().get(this.jwtConfiguration.getUserNameField()).toString();
    }

    private void verityIfIdToken(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception("JWT token is not an ID token");
        }
    }

    private void validateIssuer(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(), this.jwtConfiguration.getCognitoIdentityPoolUrl()));
        }
    }
}
