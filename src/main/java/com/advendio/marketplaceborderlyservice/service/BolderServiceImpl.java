/* (C)2022 */
package com.advendio.marketplaceborderlyservice.service;

import com.advendio.marketplaceborderlyservice.client.AuthClient;
import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import com.advendio.marketplaceborderlyservice.model.response.EncryptedData;
import com.advendio.marketplaceborderlyservice.utils.AsymmetricKey;
import com.advendio.marketplaceborderlyservice.utils.GenerateKeys;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BolderServiceImpl implements BolderService {
    private final AuthClient authClient;

    private final ObjectMapper mapper;

    private final AsymmetricKey ac;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public BolderServiceImpl(AuthClient authClient, ObjectMapper mapper, AsymmetricKey ac) {
        this.authClient = authClient;
        this.mapper = mapper;
        this.ac = ac;
    }

    @Override
    public TokenDto getToken(EncryptedData encryptedData) throws Exception {
        PrivateKey privateKey = ac.getPrivate();
        ClientRequest clientRequest =
                mapper.readValue(ac.decryptData(encryptedData, privateKey), ClientRequest.class);
        return authClient.getToken(clientRequest);
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
    public EncryptedData encryptData(ClientRequest clientRequest) throws Exception {
        byte[] encryptedData =
                ac.encyptData(mapper.writeValueAsString(clientRequest), ac.getSecretKey());
        byte[] encryptedKey = ac.encryptKey(ac.getPublic(), ac.getSecretKey());
        return new EncryptedData(encryptedData, encryptedKey);
    }
}
