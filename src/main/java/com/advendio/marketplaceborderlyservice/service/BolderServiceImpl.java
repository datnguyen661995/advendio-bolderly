package com.advendio.marketplaceborderlyservice.service;

import com.advendio.marketplaceborderlyservice.client.AuthClient;
import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class BolderServiceImpl implements BolderService {
    private final AuthClient authClient;

    private final ObjectMapper mapper = new ObjectMapper();

    public BolderServiceImpl(AuthClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public TokenDto getToken(ClientRequest clientRequest) {
        return mapper.convertValue(authClient.getToken(clientRequest).getBody(), TokenDto.class);
    }

}
