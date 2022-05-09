/* (C)2022 */
package com.advendio.marketplace.openservice.utils;

import static com.advendio.marketplace.openservice.constants.CommonConstants.CONFIG_KEYSTORE_ALIAS;
import static com.advendio.marketplace.openservice.constants.CommonConstants.CONFIG_KEYSTORE_PASSWORD;
import static com.advendio.marketplace.openservice.constants.CommonConstants.CONFIG_KEYSTORE_PATH;

import com.advendio.marketplace.openservice.model.response.EncryptedData;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class KeyUtils {
    private static final String RSA = "RSA";
    private static final String AES = "AES";
    private static final String RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1Padding";
    private static final String KEY_STORE_TYPE = "JKS";
    private static final int ASYMMETRIC_KEY_SIZE = 2048;
    private static final int SYMMETRIC_KEY_SIZE = 128;

    private final SecretKey secretKey;

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String keystorePath;
    private final String keystorePassword;
    private final String certAlias;

    private final KeyStore keyStore;

    public KeyUtils(Environment env)
            throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
        this.secretKey = generateSecretKey();
        this.keystorePath = env.getProperty(CONFIG_KEYSTORE_PATH);
        this.keystorePassword = env.getProperty(CONFIG_KEYSTORE_PASSWORD);
        this.certAlias = env.getProperty(CONFIG_KEYSTORE_ALIAS);
        log.info("****** READ KEYSTORE ********");
        this.keyStore = readKeyStore();
    }

    public PublicKey getPublic()
            throws NoSuchAlgorithmException, InvalidKeySpecException, UnrecoverableKeyException,
                    KeyStoreException {
        PrivateKey privateKey = loadThePrivateKey();
        RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey) privateKey;
        RSAPublicKeySpec publicKeySpec =
                new RSAPublicKeySpec(
                        rsaPrivateCrtKey.getModulus(), rsaPrivateCrtKey.getPublicExponent());

        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(publicKeySpec);
    }

    public SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        log.info("****** GENERATE SECRET KEY ********");
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(SYMMETRIC_KEY_SIZE);
        SecretKey result = keyGenerator.generateKey();
        log.info("Generated key: {}", result.getEncoded());
        return result;
    }

    public SecretKey getSecretKey() {
        return this.secretKey;
    }

    public byte[] encryptData(String data, SecretKey secretKey) throws GeneralSecurityException {
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
        String[] seperatedEncryptedData = encryptedData.getData().split("\\.");
        log.info("Decrypting Key");
        byte[] decryptedKey = decryptKey(privateKey, seperatedEncryptedData[1]);
        log.info("Decrypting Data");
        return decryptData(decryptedKey, seperatedEncryptedData[0]);
    }

    private byte[] decryptData(byte[] decryptedKey, String encryptedData)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
                    IllegalBlockSizeException, BadPaddingException {
        SecretKey originalKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, AES);
        Cipher aesCipher = Cipher.getInstance(AES);
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        return aesCipher.doFinal(Base64.getUrlDecoder().decode(encryptedData));
    }

    private byte[] decryptKey(PrivateKey privateKey, String encryptedKey)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1PADDING);
        cipher.init(Cipher.PRIVATE_KEY, privateKey);
        byte[] decodedKey = Base64.getUrlDecoder().decode(encryptedKey);
        return cipher.doFinal(decodedKey);
    }

    public PrivateKey loadThePrivateKey()
            throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        return (PrivateKey) this.keyStore.getKey(certAlias, keystorePassword.toCharArray());
    }

    private KeyStore readKeyStore()
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keystore = KeyStore.getInstance(KEY_STORE_TYPE);
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keystore.load(fis, keystorePassword.toCharArray());
        }
        return keystore;
    }
}
