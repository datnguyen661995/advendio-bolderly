/* (C)2022 */
package com.advendio.marketplace.openservice.service.impl;

import static com.advendio.marketplace.openservice.constants.CommonConstants.COGNITO_GRANT_TYPE_CLIENT_CREDENTIALS;
import static com.advendio.marketplace.openservice.enums.ErrorCode.COGNITO_ERROR_CLIENT_ID_IS_NOT_SUITABLE;

import com.advendio.marketplace.openservice.client.AuthClient;
import com.advendio.marketplace.openservice.enums.ErrorCode;
import com.advendio.marketplace.openservice.exception.CognitoException;
import com.advendio.marketplace.openservice.model.dto.TokenDto;
import com.advendio.marketplace.openservice.model.request.ClientRequest;
import com.advendio.marketplace.openservice.model.request.CreateClientRequest;
import com.advendio.marketplace.openservice.properties.JwtProperties;
import com.advendio.marketplace.openservice.service.CognitoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolClientRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolClientResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DescribeUserPoolClientRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DescribeUserPoolClientResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolClientsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolClientsResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.OAuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolClientType;

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
                    ClientRequest.builder()
                            .grant_type(COGNITO_GRANT_TYPE_CLIENT_CREDENTIALS)
                            .client_id(response.userPoolClient().clientId())
                            .client_secret(response.userPoolClient().clientSecret())
                            .scope(response.userPoolClient().allowedOAuthScopes())
                            .build();
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

    private UserPoolClientType describeClient(String clientId) {
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
            if (e.awsErrorDetails()
                    .errorCode()
                    .equals(ErrorCode.AWS_RESOURCE_NOT_FOUND_EXCEPTION.getCode())) {
                throw new CognitoException(
                        e.awsErrorDetails().errorCode(),
                        ErrorCode.AWS_RESOURCE_NOT_FOUND_EXCEPTION.getMessage());
            }
            throw new CognitoException(
                    e.awsErrorDetails().errorCode(), e.awsErrorDetails().errorMessage());
        }
    }

    @Override
    public TokenDto getToken(String clientId) {
        UserPoolClientType userPoolClient = this.describeClient(clientId);
        if (userPoolClient.allowedOAuthScopes().isEmpty()
                || !userPoolClient.hasAllowedOAuthFlows()) {
            throw new CognitoException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    COGNITO_ERROR_CLIENT_ID_IS_NOT_SUITABLE.getMessage());
        }
        return authClient.getToken(
                ClientRequest.builder()
                        .client_id(userPoolClient.clientId())
                        .client_secret(userPoolClient.clientSecret())
                        .grant_type(COGNITO_GRANT_TYPE_CLIENT_CREDENTIALS)
                        .scope(userPoolClient.allowedOAuthScopes())
                        .build());
    }

    @Override
    public TokenDto getToken(ClientRequest clientRequest) {
        TokenDto result = authClient.getToken(clientRequest);
        result.setClientId(clientRequest.getClient_id());
        return result;
    }
}
