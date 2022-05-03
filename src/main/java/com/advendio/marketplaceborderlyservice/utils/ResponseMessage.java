/* (C)2022 */
package com.advendio.marketplaceborderlyservice.utils;

import com.advendio.marketplaceborderlyservice.enums.ErrorCode;
import com.advendio.marketplaceborderlyservice.exception.CognitoException;
import com.advendio.marketplaceborderlyservice.exception.CustomException;
import com.advendio.marketplaceborderlyservice.model.response.CustomErrorResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseMessage {

    public static ResponseEntity<Object> responseError(CustomException customException) {
        CustomErrorResponse customErrorResponse =
                new CustomErrorResponse(
                        customException.getErrorCode(), customException.getMessage());
        return new ResponseEntity<>(customErrorResponse, customException.getStatus());
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
