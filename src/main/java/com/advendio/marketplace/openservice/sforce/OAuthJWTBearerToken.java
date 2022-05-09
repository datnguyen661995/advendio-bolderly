/* (C)2022 */
package com.advendio.marketplace.openservice.sforce;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.MessageFormat;
import java.util.Base64;
import lombok.ToString;

@ToString(of = {"keystorePath", "certAlias", "clientId", "instanceSubDomain"})
public class OAuthJWTBearerToken {
    public static final String HEADER = "{\"alg\":\"RS256\"}";
    private static final String CLAIM_TEMPLATE =
            "'{'\"iss\": \"{0}\", \"sub\": \"{1}\", \"aud\": \"{2}\", \"exp\": \"{3}\"'}'";
    public static final long TOKEN_LIVETIME_SECONDS = 300; // 5 minutes

    public static final String DEFAULT_PROTOCOL = "https://";
    public static final String DEFAULT_SUBDOMAIN = "login";
    public static final String SALESFORCE_OAUTH_TOKEN_URL_DEFAULT =
            ".salesforce.com/services/oauth2/token";

    private static final String LOGIN_URL_END = ".salesforce.com";
    private static final String LOGIN_URL_SANDBOX = "https://test.salesforce.com";
    private static final String ENCODING = "UTF-8";
    private static final String ALGORITHEM = "SHA256withRSA";
    private static final String KEY_STORE_TYPE = "JKS";

    private String keystorePath;
    private String keystorePassword;
    private String certAlias;
    private String privateKeyPassword;
    private StringBuffer token;
    private long creationTimeSeconds;
    private String username;
    private boolean sandbox;
    private String clientId;
    private String instanceSubDomain;

    public OAuthJWTBearerToken(
            String keystorePath,
            String keystorePassword,
            String certAlias,
            String privateKeyPassword,
            String clientId,
            String instanceSubDomain) {
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;
        this.certAlias = certAlias;
        this.privateKeyPassword = privateKeyPassword;
        this.clientId = clientId;
        this.instanceSubDomain = instanceSubDomain;
    }

    /**
     * recreates the JWTBearerToken on first use of this instance, if username changes or the token
     * is expired
     *
     * @param username of the Salesforce-User to login
     * @return the JWTBearerToken
     * @throws UnrecoverableKeyException
     * @throws InvalidKeyException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws FileNotFoundException
     * @throws SignatureException
     * @throws IOException
     */
    public String getToken(String username)
            throws UnrecoverableKeyException, InvalidKeyException, KeyStoreException,
                    NoSuchAlgorithmException, CertificateException, SignatureException,
                    IOException {
        return getToken(username, false);
    }

    public synchronized String getToken(String username, boolean sandbox)
            throws UnrecoverableKeyException, InvalidKeyException, KeyStoreException,
                    NoSuchAlgorithmException, CertificateException, SignatureException,
                    IOException {
        if (needToRegenerate(username, sandbox)) {
            this.sandbox = sandbox;
            this.username = username;
            generateToken();
        }
        return token.toString();
    }

    private boolean needToRegenerate(String username, boolean sandbox) {
        if (!username.equals(this.username)) {
            return true;
        }
        if (this.sandbox != sandbox) {
            return true;
        }
        if (creationTimeSeconds + TOKEN_LIVETIME_SECONDS - 10 < System.currentTimeMillis() / 1000) {
            return true;
        }
        return false;
    }

    private synchronized void generateToken()
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
                    UnrecoverableKeyException, InvalidKeyException, SignatureException {
        token = new StringBuffer();
        addJwtHeader();
        addJwtClaimsObject();
        addJwtSignature();
    }

    private void addJwtHeader() throws UnsupportedEncodingException {
        token.append(Base64.getUrlEncoder().encodeToString(HEADER.getBytes(ENCODING)));
    }

    private void addJwtClaimsObject() throws UnsupportedEncodingException {
        token.append(".");
        token.append(
                Base64.getUrlEncoder()
                        .encodeToString(createTheJwtClaimsObject().getBytes(ENCODING)));
    }

    private String createTheJwtClaimsObject() {
        String[] claimArray = new String[4];
        claimArray[0] = clientId;
        claimArray[1] = username;
        if (sandbox) {
            claimArray[2] = LOGIN_URL_SANDBOX;
        } else {
            if (instanceSubDomain == null || instanceSubDomain.isEmpty()) {
                claimArray[2] = DEFAULT_PROTOCOL + DEFAULT_SUBDOMAIN + LOGIN_URL_END;
            } else {
                claimArray[2] = DEFAULT_PROTOCOL + DEFAULT_SUBDOMAIN + LOGIN_URL_END;
            }
        }
        creationTimeSeconds = System.currentTimeMillis() / 1000;
        claimArray[3] = Long.toString(creationTimeSeconds + TOKEN_LIVETIME_SECONDS);
        MessageFormat claims;
        claims = new MessageFormat(CLAIM_TEMPLATE);
        return claims.format(claimArray);
    }

    private void addJwtSignature()
            throws InvalidKeyException, UnrecoverableKeyException, NoSuchAlgorithmException,
                    KeyStoreException, CertificateException, FileNotFoundException,
                    SignatureException, IOException {
        String signedPayload = createJwtSignature();
        token.append(".");
        token.append(signedPayload);
    }

    private String createJwtSignature()
            throws NoSuchAlgorithmException, InvalidKeyException, UnrecoverableKeyException,
                    KeyStoreException, CertificateException, IOException, SignatureException {
        Signature signature = Signature.getInstance(ALGORITHEM);
        signature.initSign(loadThePrivateKey());
        signature.update(token.toString().getBytes(ENCODING));
        return Base64.getUrlEncoder().encodeToString(signature.sign());
    }

    private PrivateKey loadThePrivateKey()
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
                    UnrecoverableKeyException {
        KeyStore keystore = KeyStore.getInstance(KEY_STORE_TYPE);
        java.io.FileInputStream fis = null;
        try {
            fis = new FileInputStream(keystorePath);
            keystore.load(fis, keystorePassword.toCharArray());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return (PrivateKey) keystore.getKey(certAlias, privateKeyPassword.toCharArray());
    }
}
