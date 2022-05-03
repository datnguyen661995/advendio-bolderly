/* (C)2022 */
package com.advendio.marketplaceborderlyservice.authenticate;

import com.nimbusds.jwt.JWTClaimsSet;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthentication extends AbstractAuthenticationToken {
    private final Object principal;
    private JWTClaimsSet jwtClaimsSet;

    public JwtAuthentication(
            Object principal,
            JWTClaimsSet jwtClaimsSet,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.jwtClaimsSet = jwtClaimsSet;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public JWTClaimsSet getJwtClaimsSet() {
        return this.jwtClaimsSet;
    }
}
