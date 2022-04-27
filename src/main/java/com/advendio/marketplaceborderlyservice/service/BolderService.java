package com.advendio.marketplaceborderlyservice.service;


import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;

import java.util.Map;

public interface BolderService {
    TokenDto getToken(ClientRequest clientRequest);
}
