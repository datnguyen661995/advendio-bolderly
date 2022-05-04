/* (C)2022 */
package com.advendio.marketplaceborderlyservice.service.impl;

import static com.advendio.marketplaceborderlyservice.constants.CommonConstants.COGNITO_GRANT_TYPE_CLIENT_CREDENTIALS;

import com.advendio.marketplaceborderlyservice.client.AuthClient;
import com.advendio.marketplaceborderlyservice.exception.CognitoException;
import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import com.advendio.marketplaceborderlyservice.model.request.CreateClientRequest;
import com.advendio.marketplaceborderlyservice.properties.JwtProperties;
import com.advendio.marketplaceborderlyservice.service.CognitoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

@Service
public class CognitoServiceImpl implements CognitoService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JwtProperties jwtProperties;

    private CognitoIdentityProviderClient cognitoIdentityProviderClient;

    private final AuthClient authClient;

    public CognitoServiceImpl(JwtProperties jwtProperties, AuthClient authClient) {
        this.jwtProperties = jwtProperties;
        this.authClient = authClient;
    }

    private CognitoIdentityProviderClient getCognitoClient() {
        if (null == this.cognitoIdentityProviderClient) {
            this.cognitoIdentityProviderClient =
                    CognitoIdentityProviderClient.builder()
                            .region(Region.of(jwtProperties.getRegion()))
                            .build();
        }
        return this.cognitoIdentityProviderClient;
    }

    @Override
    public TokenDto createPoolClientAndGetToken(CreateClientRequest createClientRequest) {
        try {
            CreateUserPoolClientResponse response =
                    getCognitoClient()
                            .createUserPoolClient(
                                    CreateUserPoolClientRequest.builder()
                                            .allowedOAuthFlowsUserPoolClient(true)
                                            .clientName(createClientRequest.getClientName())
                                            .userPoolId(jwtProperties.getUserPoolId())
                                            .generateSecret(true)
                                            .allowedOAuthFlows(OAuthFlowType.CLIENT_CREDENTIALS)
                                            .allowedOAuthScopes(createClientRequest.getScopes())
                                            .build());

            ClientRequest clientRequest =
                    new ClientRequest(
                            COGNITO_GRANT_TYPE_CLIENT_CREDENTIALS,
                            response.userPoolClient().clientId(),
                            response.userPoolClient().clientSecret(),
                            response.userPoolClient().allowedOAuthScopes());
            log.info("Created new client: {}", createClientRequest.getClientName());
            return this.getToken(clientRequest);
        } catch (CognitoIdentityProviderException e) {
            throw new CognitoException(
                    e.awsErrorDetails().errorCode(), e.awsErrorDetails().errorMessage());
        }
    }

    @Override
    public ListUserPoolClientsResponse listAllUserPoolClients() {

        try {
            return getCognitoClient()
                    .listUserPoolClients(
                            ListUserPoolClientsRequest.builder()
                                    .userPoolId(jwtProperties.getUserPoolId())
                                    .build());
        } catch (CognitoIdentityProviderException e) {
            throw new CognitoException(
                    e.awsErrorDetails().errorCode(), e.awsErrorDetails().errorMessage());
        }
    }

    private UserPoolClientType describleClient(String clientId) {
        try {
            DescribeUserPoolClientResponse response =
                    getCognitoClient()
                            .describeUserPoolClient(
                                    DescribeUserPoolClientRequest.builder()
                                            .clientId(clientId)
                                            .userPoolId(jwtProperties.getUserPoolId())
                                            .build());
            return response.userPoolClient();
        } catch (CognitoIdentityProviderException e) {
            throw new CognitoException(
                    e.awsErrorDetails().errorCode(), e.awsErrorDetails().errorMessage());
        }
    }

    @Override
    public TokenDto getToken(ClientRequest clientRequest) {
        UserPoolClientType userPoolClient = this.describleClient(clientRequest.getClient_id());

        clientRequest.setClient_secret(userPoolClient.clientSecret());
        return authClient.getToken(clientRequest);
    }
}
