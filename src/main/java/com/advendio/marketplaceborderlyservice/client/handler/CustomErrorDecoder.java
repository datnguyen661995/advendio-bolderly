/* (C)2022 */
package com.advendio.marketplaceborderlyservice.client.handler;

import com.advendio.marketplaceborderlyservice.enums.ErrorCode;
import com.advendio.marketplaceborderlyservice.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String requestUrl = response.request().url();
        HttpStatus status = HttpStatus.valueOf(response.status());
        ResponseError responseError;
        try (Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {
            responseError = new ObjectMapper().readValue(reader, ResponseError.class);
        } catch (Exception e) {
            responseError =
                    new ResponseError().setErrorMessage(ErrorCode.ERROR_500_NAME.getMessage());
            log.error(ErrorCode.ERROR_WHILE_PARSE_CLIENT_ERROR.getMessage(), e);
        }
        log.info(
                ErrorCode.ERROR_WHILE_REQUEST_TO_ERROR.getMessage(),
                requestUrl,
                responseError.getMessage());
        if (status.is5xxServerError()) {
            if (status.value() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                throw new CustomException(
                        HttpStatus.INTERNAL_SERVER_ERROR, responseError.getMessage());
            }
        } else if (status.is4xxClientError()) {
            if (status.value() == HttpStatus.NOT_FOUND.value()) {
                throw new CustomException(
                        HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    null != responseError.getMessage()
                            ? responseError.getMessage()
                            : responseError.getErrorMessage());
        }
        throw new CustomException(
                HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_500_NAME.getMessage());
    }
}
