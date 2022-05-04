/* (C)2022 */
package com.advendio.marketplaceborderlyservice.utils;

import com.advendio.marketplaceborderlyservice.model.response.EncryptedData;
import com.advendio.marketplaceborderlyservice.properties.BolderlyProperties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
public class AsymmetricKey {
    private static final String RSA = "RSA";
    private static final String AES = "AES";
    private static final String RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1Padding";

    private final KeyPair keyPair;
    private final SecretKey secretKey;
    private final BolderlyProperties bolderlyProperties;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public AsymmetricKey(BolderlyProperties bolderlyProperties)
            throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.secretKey = generateSecretKey();
        this.keyPair = generateAsymmetricKey();
        this.bolderlyProperties = bolderlyProperties;
    }

    public PrivateKey getPrivate() throws IOException {
        // TODO: public key is stored somewhere, implement how to retrieve public key later
        writeToFile("KeyPair/privateKey.txt", keyPair.getPrivate().getEncoded());
        return keyPair.getPrivate();
    }

    public PublicKey getPublic() {
        return keyPair.getPublic();
    }

    public SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        log.info("****** GENERATE SECRET KEY ********");
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(128);
        SecretKey result = keyGenerator.generateKey();
        log.info("Generated key: {}", result.getEncoded());
        return result;
    }

    public KeyPair generateAsymmetricKey() throws NoSuchAlgorithmException {
        log.info("****** GENERATED ASYMMETRIC KEY ********");
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(RSA);
        keyGenerator.initialize(2048);
        return keyGenerator.generateKeyPair();
    }

    public SecretKey getSecretKey() {
        return this.secretKey;
    }

    public byte[] encyptData(String data, SecretKey secretKey) throws GeneralSecurityException {
        log.info("****** ENCRYPTED DATA ********");
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data.getBytes());
    }

    public byte[] encryptKey(PublicKey publicKey, SecretKey secretKey)
            throws GeneralSecurityException {
        log.info("****** ENCRYPT KEY ********");
        Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1PADDING);
        cipher.init(Cipher.PUBLIC_KEY, publicKey);
        byte[] result = cipher.doFinal(secretKey.getEncoded());
        log.info("Encrypted key: {}", result);
        return result;
    }

    public byte[] decryptData(EncryptedData encryptedData, PrivateKey privateKey)
            throws GeneralSecurityException {
        log.info("****** DECRYPT DATA ********");

        Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1PADDING);
        cipher.init(Cipher.PRIVATE_KEY, privateKey);
        byte[] decryptedKey = cipher.doFinal(encryptedData.getKey());
        log.info("Decrypted Key: {}", decryptedKey);

        SecretKey originalKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, AES);
        Cipher aesCipher = Cipher.getInstance(AES);
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        return aesCipher.doFinal(encryptedData.getData());
    }

    public byte[] decryptData(EncryptedData encryptedData)
            throws GeneralSecurityException, IOException {
        log.info("****** DECRYPT DATA ********");
        PrivateKey privateKey = getFilePrivateKey();

        Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1PADDING);
        cipher.init(Cipher.PRIVATE_KEY, privateKey);
        byte[] decryptedKey = cipher.doFinal(encryptedData.getKey());
        log.info("Decrypted Key: {}", decryptedKey);

        SecretKey originalKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, AES);
        Cipher aesCipher = Cipher.getInstance(AES);
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        return aesCipher.doFinal(encryptedData.getData());
    }

    /**
     * * Get PrivateKey
     *
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PrivateKey getFilePrivateKey()
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Resource resource = new ClassPathResource(bolderlyProperties.getPrivateKey());
        InputStream inputStream = resource.getInputStream();
        byte[] key = FileCopyUtils.copyToByteArray(inputStream);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(key);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        return kf.generatePrivate(spec);
    }

    /**
     * * Store File
     *
     * @param path
     * @param key
     * @throws IOException
     */
    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }
}
