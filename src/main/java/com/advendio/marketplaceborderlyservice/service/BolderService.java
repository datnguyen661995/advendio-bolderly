/* (C)2022 */
package com.advendio.marketplaceborderlyservice.service;

import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import com.advendio.marketplaceborderlyservice.model.request.CreateClientRequest;
import com.advendio.marketplaceborderlyservice.model.response.EncryptedData;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface BolderService {
    ClientRequest decryptClientRequest(EncryptedData encryptedData);

    CreateClientRequest decryptCreateData(EncryptedData encryptedData);

    void genKey() throws NoSuchAlgorithmException, NoSuchProviderException;

    EncryptedData encryptData(Object request);
}
