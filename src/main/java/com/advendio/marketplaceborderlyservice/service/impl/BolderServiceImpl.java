/* (C)2022 */
package com.advendio.marketplaceborderlyservice.service.impl;

import com.advendio.marketplaceborderlyservice.exception.CustomException;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import com.advendio.marketplaceborderlyservice.model.request.CreateClientRequest;
import com.advendio.marketplaceborderlyservice.model.response.EncryptedData;
import com.advendio.marketplaceborderlyservice.service.BolderService;
import com.advendio.marketplaceborderlyservice.utils.AsymmetricKey;
import com.advendio.marketplaceborderlyservice.utils.GenerateKeys;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BolderServiceImpl implements BolderService {

    private final ObjectMapper mapper;

    private final AsymmetricKey ac;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public BolderServiceImpl(ObjectMapper mapper, AsymmetricKey ac) {
        this.mapper = mapper;
        this.ac = ac;
    }

    @Override
    public ClientRequest decryptClientRequest(EncryptedData encryptedData) {
        PrivateKey privateKey = ac.getPrivate();
        try {
            return mapper.readValue(ac.decryptData(encryptedData, privateKey), ClientRequest.class);
        } catch (IOException | GeneralSecurityException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public void genKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        GenerateKeys gk = new GenerateKeys(2048);
        try {
            gk.createKeys();
            gk.writeToFile("KeyPair/publicKey.txt", gk.getPublicKey().getEncoded());
            gk.writeToFile("KeyPair/privateKey.txt", gk.getPrivateKey().getEncoded());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public EncryptedData encryptData(Object request) {
        byte[] encryptedData = new byte[0];
        try {
            encryptedData = ac.encyptData(mapper.writeValueAsString(request), ac.getSecretKey());
            byte[] encryptedKey = ac.encryptKey(ac.getPublic(), ac.getSecretKey());
            return new EncryptedData(encryptedData, encryptedKey);
        } catch (GeneralSecurityException | JsonProcessingException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public CreateClientRequest decryptCreateData(EncryptedData encryptedData) {
        PrivateKey privateKey = ac.getPrivate();
        try {
            return mapper.readValue(
                    ac.decryptData(encryptedData, privateKey), CreateClientRequest.class);
        } catch (IOException | GeneralSecurityException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
