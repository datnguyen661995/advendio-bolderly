package com.advendio.marketplaceborderlyservice.service;

import com.advendio.marketplaceborderlyservice.client.AuthClient;
import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import com.advendio.marketplaceborderlyservice.model.response.EncryptedData;
import com.advendio.marketplaceborderlyservice.properties.KeyProperties;
import com.advendio.marketplaceborderlyservice.utils.AsymmetricCryptography;
import com.advendio.marketplaceborderlyservice.utils.GenerateKeys;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

@Service
public class BolderServiceImpl implements BolderService {
    private final AuthClient authClient;

    private final ObjectMapper mapper;

    private final ModelMapper modelMapper;

    private final AsymmetricCryptography ac;

    private final KeyProperties keyProperties;

    public BolderServiceImpl(AuthClient authClient, ObjectMapper mapper, ModelMapper modelMapper, AsymmetricCryptography ac, KeyProperties keyProperties) {
        this.authClient = authClient;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
        this.ac = ac;
        this.keyProperties = keyProperties;
    }


    @Override
    public TokenDto getToken(EncryptedData encryptedData) throws Exception {
        PublicKey publicKey = ac.getPublic(keyProperties.getPublicKey());
        String decryptedMsg = ac.decryptText(encryptedData.getData(), publicKey);
        ClientRequest clientRequest = mapper.readValue(decryptedMsg, ClientRequest.class);
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
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void encryptAndDecryptText(String text) throws Exception {
        PrivateKey privateKey = ac.getPrivate(keyProperties.getPrivateKey());
        PublicKey publicKey = ac.getPublic(keyProperties.getPublicKey());
        String encrypted_msg = ac.encryptText(text, privateKey);
        String decrypted_msg = ac.decryptText(encrypted_msg, publicKey);

        System.out.println("Original Message: " + text +
                "\nEncrypted Message: " + encrypted_msg
                + "\nDecrypted Message: " + decrypted_msg);

        if (new File("KeyPair/text.txt").exists()) {
            ac.encryptFile(ac.getFileInBytes(new File("KeyPair/text.txt")),
                    new File("KeyPair/text_encrypted.txt"), privateKey);
            ac.decryptFile(ac.getFileInBytes(new File("KeyPair/text_encrypted.txt")),
                    new File("KeyPair/text_decrypted.txt"), publicKey);
        } else {
            System.out.println("Create a file text.txt under folder KeyPair");
        }
    }

    @Override
    public EncryptedData encryptData(ClientRequest clientRequest) throws Exception {
        PrivateKey privateKey = ac.getPrivate(keyProperties.getPrivateKey());
        return new EncryptedData(ac.encryptText(mapper.writeValueAsString(clientRequest), privateKey));
    }
}
