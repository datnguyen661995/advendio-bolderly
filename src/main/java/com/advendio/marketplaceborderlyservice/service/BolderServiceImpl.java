package com.advendio.marketplaceborderlyservice.service;

import com.advendio.marketplaceborderlyservice.client.AuthClient;
import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import com.advendio.marketplaceborderlyservice.properties.BolderlyProperties;
import com.advendio.marketplaceborderlyservice.properties.JwtProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BolderServiceImpl implements BolderService {
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String CLIENT_ID_PARAM = "client_id";

    @Autowired
    AuthClient authClient;

    @Autowired
    JwtProperties jwtProperties;

    @Override
    public Map<String, Object> getToken(ClientRequest clientRequest) {
        Map<String, String> body = buildHeader(clientRequest).toSingleValueMap();
        Map<String, Object> token= authClient.getToken(body).getBody();
        if (null != token) {
            return token;
        }
        return null;
    }

    private HttpHeaders buildHeader(ClientRequest clientRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(getDefaultTokenHeaders());
        if (StringUtils.isNotBlank(clientRequest.getGrantType()) && StringUtils.isNotBlank(clientRequest.getClientId())) {
            headers.set(GRANT_TYPE_PARAM, clientRequest.getGrantType());
            headers.set(CLIENT_ID_PARAM, clientRequest.getClientId());
        }
        return headers;
    }

    private HttpHeaders getDefaultTokenHeaders() {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.APPLICATION_FORM_URLENCODED;
        headers.setContentType(mediaType);
        return headers;
    }
}
