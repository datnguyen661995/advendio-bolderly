package com.advendio.marketplaceborderlyservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String errorMsg;
    private final String errorCode;

    public CustomException() {
        this.status = null;
        this.errorCode = null;
        this.errorMsg = null;
    }

    public CustomException(HttpStatus status, String message, String code) {
        super();
        this.status = status;
        this.errorMsg = message;
        this.errorCode = code;
    }

    public CustomException(HttpStatus status, String code) {
        super();
        this.status = status;
        this.errorMsg = null;
        this.errorCode = code;
    }

    public CustomException(String message, String code) {
        super();
        this.status = HttpStatus.BAD_GATEWAY;
        this.errorMsg = message;
        this.errorCode = code;
    }
}
