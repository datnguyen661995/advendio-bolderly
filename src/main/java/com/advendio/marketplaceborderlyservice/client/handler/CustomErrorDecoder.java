package com.advendio.marketplaceborderlyservice.client.handler;

import com.advendio.marketplaceborderlyservice.exception.CustomErrorMessage;
import com.advendio.marketplaceborderlyservice.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

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
            responseError = new ResponseError().setErrorMessage(CustomErrorMessage.ERROR_500_NAME);
            log.error(CustomErrorMessage.ERROR_WHILE_PARSE_CLIENT_ERROR, e);
        }
        log.info(CustomErrorMessage.ERROR_WHILE_REQUEST_TO_ERROR, requestUrl, responseError.getMessage());
        if (status.is5xxServerError()) {
            if (status.value() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, responseError.getMessage());
            }
        } else if (status.is4xxClientError()) {
            if (status.value() == HttpStatus.NOT_FOUND.value()) {
                throw new CustomException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            throw new CustomException(HttpStatus.BAD_REQUEST, null != responseError.getMessage() ? responseError.getMessage() : responseError.getErrorMessage());
        }
        throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, CustomErrorMessage.ERROR_500_NAME);
    }
}
