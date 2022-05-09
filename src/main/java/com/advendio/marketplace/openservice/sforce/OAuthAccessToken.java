/* (C)2022 */
package com.advendio.marketplace.openservice.sforce;

import com.advendio.marketplace.openservice.aspect.MethodCache;
import com.advendio.marketplace.openservice.client.SForceClient;
import com.advendio.marketplace.openservice.enums.ErrorCode;
import com.advendio.marketplace.openservice.exception.BusinessException;
import com.advendio.marketplace.openservice.model.request.SForceClientRequest;
import com.advendio.marketplace.openservice.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuthAccessToken {
    private static final String GRANT_TYPE_JWT_BEARER =
            "urn:ietf:params:oauth:grant-type:jwt-bearer";
    private static final String DEFAULT_PROTOCOL = "https://";
    private static final String DEFAULT_SUBDOMAIN = "login";
    private static final String SALESFORCE_OAUTH_TOKEN_URL_REMAIN =
            ".salesforce.com/services/oauth2/token";
    private static final String SALESFORCE_OAUTH_TOKEN_URL_SANDBOX = "/services/oauth2/token";

    @Autowired private SForceClient sForceClient;

    @MethodCache
    public String getAccessToken(
            String userName,
            boolean sandbox,
            OAuthJWTBearerToken bearerTokenGenerator,
            String clientId,
            String instanceSubDomain)
            throws UnrecoverableKeyException, InvalidKeyException, KeyStoreException,
                    NoSuchAlgorithmException, CertificateException, SignatureException, IOException,
                    URISyntaxException {
        return getAccessTokenByBearerToken(
                bearerTokenGenerator.getToken(userName, sandbox),
                sandbox,
                clientId,
                instanceSubDomain);
    }

    public String getRefreshAccessToken(
            String userName,
            boolean sandbox,
            OAuthJWTBearerToken bearerTokenGenerator,
            String clientId,
            String instanceSubDomain)
            throws UnrecoverableKeyException, InvalidKeyException, KeyStoreException,
                    NoSuchAlgorithmException, CertificateException, SignatureException, IOException,
                    URISyntaxException {
        return getAccessTokenByBearerToken(
                bearerTokenGenerator.getToken(userName, sandbox),
                sandbox,
                clientId,
                instanceSubDomain);
    }

    private String getAccessTokenByBearerToken(
            String bearerToken, boolean sandbox, String clientId, String instanceSubDomain)
            throws IOException, URISyntaxException {
        String url;
        if (sandbox) {
            url = instanceSubDomain + SALESFORCE_OAUTH_TOKEN_URL_SANDBOX;
        } else {
            url = DEFAULT_PROTOCOL + DEFAULT_SUBDOMAIN + SALESFORCE_OAUTH_TOKEN_URL_REMAIN;
        }

        // Create Params Request
        SForceClientRequest postParams =
                new SForceClientRequest()
                        .setGrant_type(GRANT_TYPE_JWT_BEARER)
                        .setAssertion(bearerToken);
        String response = sForceClient.getAccessToken(new URI(url), postParams);
        if (StringUtils.isBlank(response)) {
            throw new BusinessException(ErrorCode.ERROR_CREATE_JWT_ACCESS_TOKEN);
        }
        JsonNode token = JsonUtils.getJsonNodeFromJsonString(response);
        return token.get("access_token").asText();
    }
}
