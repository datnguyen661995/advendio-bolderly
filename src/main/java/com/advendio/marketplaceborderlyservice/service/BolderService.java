/* (C)2022 */
package com.advendio.marketplaceborderlyservice.service;

import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import com.advendio.marketplaceborderlyservice.model.response.EncryptedData;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface BolderService {
    TokenDto getToken(EncryptedData encryptedData) throws Exception;

    void genKey() throws NoSuchAlgorithmException, NoSuchProviderException;

    EncryptedData encryptData(ClientRequest clientRequest) throws Exception;
}
