package com.advendio.marketplaceborderlyservice.service;

import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.CreateClientRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolClientsResponse;

public interface CognitoService {
    TokenDto createPoolClientAndGetToken(CreateClientRequest clientRequest);

    ListUserPoolClientsResponse listAllUserPoolClients();
}
