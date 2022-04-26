package com.advendio.marketplaceborderlyservice.service;

import com.advendio.marketplaceborderlyservice.client.AuthClient;
import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BolderServiceImpl implements BolderService {
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String CLIENT_ID_PARAM = "client_id";

    @Autowired
    AuthClient authClient;

    @Override
    public TokenDto getToken(ClientRequest clientRequest) {
        Map<String, String> body = new HashMap<>();
        body.put(GRANT_TYPE_PARAM, clientRequest.getGrantType());
        body.put(CLIENT_ID_PARAM, clientRequest.getClientId());
        TokenDto token = authClient.getToken(body);
        if (null != token) {
            return token;
        }
        return null;
    }
}
