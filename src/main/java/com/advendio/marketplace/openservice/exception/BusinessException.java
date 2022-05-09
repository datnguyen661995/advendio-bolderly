/* (C)2022 */
package com.advendio.marketplace.openservice.exception;

import com.advendio.marketplace.openservice.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BusinessException extends RuntimeException {
    private final HttpStatus status;
    private final String errorMsg;
    private final String errorCode;

    public BusinessException() {
        this.status = null;
        this.errorCode = null;
        this.errorMsg = null;
    }

    public BusinessException(String errorMsg) {
        this.status = null;
        this.errorCode = null;
        this.errorMsg = errorMsg;
    }

    public BusinessException(ErrorCode errorCode, Object... params) {
        super(String.format(errorCode.getMessage(), params));
        this.errorCode = errorCode.getCode();
        this.status = null;
        this.errorMsg = errorCode.getMessage();
    }

    public BusinessException(HttpStatus status, String message, String code) {
        super();
        this.status = status;
        this.errorMsg = message;
        this.errorCode = code;
    }

    public BusinessException(HttpStatus status, String code) {
        super();
        this.status = status;
        this.errorMsg = null;
        this.errorCode = code;
    }

    public BusinessException(String message, String code) {
        super();
        this.status = HttpStatus.BAD_GATEWAY;
        this.errorMsg = message;
        this.errorCode = code;
    }

    public BusinessException(ErrorCode errorCode, HttpStatus status, Throwable ex) {
        super(ex);
        this.errorMsg = null;
        this.status = status;
        this.errorCode = errorCode.getCode();
    }
}
