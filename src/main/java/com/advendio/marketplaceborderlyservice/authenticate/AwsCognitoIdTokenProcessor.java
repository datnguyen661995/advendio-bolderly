package com.advendio.marketplaceborderlyservice.authenticate;

import com.advendio.marketplaceborderlyservice.exception.CognitoException;
import com.advendio.marketplaceborderlyservice.properties.JwtProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@Component
@AllArgsConstructor
@Slf4j
public class AwsCognitoIdTokenProcessor {
    private static final String Bearer_ = "Bearer ";
    private static final String CLIENT_ID = "client_id";
    private static final String TOKEN_USE = "token_use";

    private final JwtProperties jwtProperties;
    private final ConfigurableJWTProcessor configurableJWTProcessor;

    public Authentication authenticate(HttpServletRequest request) throws BadJOSEException, ParseException, JOSEException {
        String idToken = request.getHeader(this.jwtProperties.getHttpHeader());
        if (null == idToken) {
            throw new CognitoException(HttpStatus.UNAUTHORIZED, CognitoException.NO_TOKEN_PROVIDED_EXCEPTION);
        }
        JWTClaimsSet claims = null;
        claims = this.configurableJWTProcessor.process(this.getBearerToken(idToken), null);
        validateIssuer(claims);
        verityIfIdToken(claims);
        return new JwtAuthentication(null, claims, null);
    }

    private String getBearerToken(String token) {
        return token.startsWith(Bearer_) ? token.substring(Bearer_.length()) : token;
    }

    private void verityIfIdToken(JWTClaimsSet claims) {
        if (!claims.getClaim(TOKEN_USE).equals(jwtProperties.getTokenUse())) {
            throw new CognitoException(HttpStatus.UNAUTHORIZED,
                    CognitoException.NOT_TOKEN_USE_EXCEPTION);
        }
        // TODO: Check client ID in pool later
//        if (!claims.getClaim(CLIENT_ID).equals(jwtProperties.getClientId())) {
//            throw new CognitoException(HttpStatus.UNAUTHORIZED,
//                    CognitoException.NOT_CLIENT_ID_EXCEPTION);
//        }
    }

    private void validateIssuer(JWTClaimsSet claims) {
        if (!claims.getIssuer().equals(this.jwtProperties.getCognitoIdentityPoolUrl())) {
            throw new CognitoException(CognitoException.INVALID_TOKEN, String.format(CognitoException.INVALID_TOKEN_EXCEPTION_CODE, claims.getIssuer(), this.jwtProperties.getCognitoIdentityPoolUrl()));
        }
    }
}
