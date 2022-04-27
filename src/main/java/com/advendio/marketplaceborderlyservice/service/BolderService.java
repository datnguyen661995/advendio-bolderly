package com.advendio.marketplaceborderlyservice.service;


import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface BolderService {
    TokenDto getToken(ClientRequest clientRequest);
    void genKey() throws NoSuchAlgorithmException, NoSuchProviderException;
    void encryptAndDecryptText(String text) throws Exception;
}
