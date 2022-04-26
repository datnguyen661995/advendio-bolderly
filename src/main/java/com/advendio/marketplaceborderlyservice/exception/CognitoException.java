package com.advendio.marketplaceborderlyservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CognitoException extends RuntimeException {
    public static final String NO_TOKEN_FOUND = "NO_TOKEN_FOUND";
    public static final String NO_TOKEN_PROVIDED_EXCEPTION = "No token found in Http Authorization Header";
    public static final String INVALID_TOKEN = "Invalid Token";
    public static final String INVALID_TOKEN_EXCEPTION_CODE = "Issuer %s in JWT token doesn't match cognito idp %s";
    public static final String NOT_A_TOKEN_EXCEPTION = "JWT Token doesn't seem to be an ID Token";
    public static final String NOT_CLIENT_ID_EXCEPTION = "JWT Token doesn't seem to be client-id";
    public static final String NOT_TOKEN_USE_EXCEPTION = "JWT Token doesn't seem to be token-use";

    private final HttpStatus status;
    private final String code;
    private final String message;

    public CognitoException() {
        this.status = null;
        this.code = null;
        this.message = null;
    }

    public CognitoException(String message) {
        super();
        this.status = null;
        this.code = null;
        this.message = message;
    }

    public CognitoException(String code, String message) {
        super();
        this.status = null;
        this.code = code;
        this.message = message;
    }

    public CognitoException(HttpStatus status, String code, String message) {
        super();
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public CognitoException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.code = null;
        this.message = message;
    }
}
