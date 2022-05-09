/* (C)2022 */
package com.advendio.marketplace.openservice.service.impl;

import com.advendio.marketplace.openservice.exception.BusinessException;
import com.advendio.marketplace.openservice.model.request.CreateClientRequest;
import com.advendio.marketplace.openservice.model.response.EncryptedData;
import com.advendio.marketplace.openservice.service.EncryptionService;
import com.advendio.marketplace.openservice.utils.KeyUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    private final ObjectMapper mapper;

    private final KeyUtils keyUtils;

    public EncryptionServiceImpl(ObjectMapper mapper, KeyUtils keyUtils) {
        this.mapper = mapper;
        this.keyUtils = keyUtils;
    }

    @Override
    public EncryptedData encryptData(Object request) {
        byte[] encryptedData = new byte[0];
        StringBuilder data = new StringBuilder();
        try {
            encryptedData =
                    keyUtils.encryptData(
                            mapper.writeValueAsString(request), keyUtils.getSecretKey());
            data.append(Base64.getUrlEncoder().encodeToString(encryptedData));
            data.append(".");
            byte[] encryptedKey =
                    keyUtils.encryptKey(keyUtils.getPublic(), keyUtils.getSecretKey());
            data.append(Base64.getUrlEncoder().encodeToString(encryptedKey));
            return new EncryptedData(data.toString());
        } catch (GeneralSecurityException | IOException e) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public CreateClientRequest decryptCreateData(EncryptedData encryptedData) {
        try {
            PrivateKey privateKey = keyUtils.loadThePrivateKey();
            return mapper.readValue(
                    keyUtils.decryptData(encryptedData, privateKey), CreateClientRequest.class);
        } catch (IOException | GeneralSecurityException e) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
