/* (C)2022 */
package com.advendio.marketplace.openservice.utils;

import com.advendio.marketplace.openservice.enums.ErrorCode;
import com.advendio.marketplace.openservice.exception.BusinessException;
import com.advendio.marketplace.openservice.exception.CognitoException;
import com.advendio.marketplace.openservice.model.response.CustomErrorResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseMessage {

    public static ResponseEntity<Object> responseError(BusinessException businessException) {
        CustomErrorResponse customErrorResponse =
                new CustomErrorResponse(
                        businessException.getErrorCode(), businessException.getMessage());
        return new ResponseEntity<>(customErrorResponse, businessException.getStatus());
    }

    public static ResponseEntity<Object> responseError(CognitoException e) {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(e.getMessage());
        return new ResponseEntity<>(customErrorResponse, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<Object> responseError() {
        CustomErrorResponse customErrorResponse =
                new CustomErrorResponse(ErrorCode.ERROR_500_NAME.getMessage());
        return new ResponseEntity<>(customErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
