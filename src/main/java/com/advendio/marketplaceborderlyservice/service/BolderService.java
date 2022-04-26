package com.advendio.marketplaceborderlyservice.service;


import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
public interface BolderService {
    TokenDto getToken(ClientRequest clientRequest);
}
