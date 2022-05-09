/* (C)2022 */
package com.advendio.marketplace.openservice.utils;

import com.advendio.marketplace.openservice.enums.ErrorCode;
import com.advendio.marketplace.openservice.exception.BusinessException;
import com.sforce.async.AsyncApiException;
import com.sforce.async.AsyncExceptionCode;
import com.sforce.soap.enterprise.fault.ExceptionCode;
import com.sforce.soap.enterprise.fault.UnexpectedErrorFault;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

/** Support Function Utility Common App */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class AppUtils {

    public static void throwBusinessException(Exception e, HttpStatus status, ErrorCode errorCode) {
        if (e instanceof BusinessException) {
            throw (BusinessException) e;
        }
        boolean isErrorInvalidSessionId = checkExceptionIsInvalidSessionId(e);
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
        if (isErrorInvalidSessionId) {
            throw new BusinessException(errorCode, e, isErrorInvalidSessionId);
        }
        throw new BusinessException(errorCode, status, e);
    }

    public static boolean checkExceptionIsInvalidSessionId(Exception e) {
        // Exception For Call SOAP API
        if (e instanceof UnexpectedErrorFault) {
            UnexpectedErrorFault unexpectedErrorFault = (UnexpectedErrorFault) e;
            return unexpectedErrorFault.getExceptionCode() == ExceptionCode.INVALID_SESSION_ID;
        }
        // Exception For Call BULK API
        if (e instanceof AsyncApiException) {
            AsyncApiException asyncApiException = (AsyncApiException) e;
            return asyncApiException.getExceptionCode() == AsyncExceptionCode.InvalidSessionId;
        }
        // Exception For Call HTTP API
        if (e instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) e;
            return httpClientErrorException
                    .getResponseBodyAsString()
                    .contains(ExceptionCode.INVALID_SESSION_ID.name());
        }
        return false;
    }
}
