/* (C)2022 */
package com.advendio.marketplace.openservice.service;

import com.advendio.marketplace.openservice.model.dto.TokenDto;
import com.advendio.marketplace.openservice.model.request.ClientRequest;
import com.advendio.marketplace.openservice.model.request.CreateClientRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolClientsResponse;

public interface CognitoService {
    TokenDto createPoolClientAndGetToken(CreateClientRequest clientRequest);

    ListUserPoolClientsResponse listAllUserPoolClients();

    TokenDto getToken(String clientId);

    TokenDto getToken(ClientRequest clientRequest);
}
