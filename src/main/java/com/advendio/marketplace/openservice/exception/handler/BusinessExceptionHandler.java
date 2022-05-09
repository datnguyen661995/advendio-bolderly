/* (C)2022 */
package com.advendio.marketplace.openservice.exception.handler;

import com.advendio.marketplace.openservice.enums.ErrorCode;
import com.advendio.marketplace.openservice.exception.BusinessException;
import com.advendio.marketplace.openservice.exception.CognitoException;
import com.advendio.marketplace.openservice.model.response.ErrorResponse;
import com.advendio.marketplace.openservice.utils.Contants;
import com.advendio.marketplace.openservice.utils.ResponseMessage;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class BusinessExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e) {
        log.error("[Marketplace Open Service] Exception: {}", e.getMessage());
        return ResponseMessage.responseError();
    }

    @ExceptionHandler(CognitoException.class)
    public final ResponseEntity<Object> handleException(CognitoException e) {
        log.error("[Marketplace Open Service] Exception: {}", e.getMessage());
        return ResponseMessage.responseError(e);
    }

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<Object> handleCustomException(BusinessException e) {
        log.error("[Marketplace Open Service] CustomException: {}", e.getMessage());
        return ResponseMessage.responseError(e);
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public final ResponseEntity<Object> handleSocketTimeoutException(SocketTimeoutException e) {
        log.error("[Marketplace Open Service] SocketTimeoutException: {}", e.getMessage());
        return ResponseMessage.responseError(
                new BusinessException(
                        HttpStatus.BAD_REQUEST,
                        String.format(
                                ErrorCode.ERROR_API_TIME_OUT_NAME.getMessage(), e.getMessage())));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException e) {
        log.error("[Marketplace Open Service] ConstraintViolationException: {}", e.getMessage());
        String message =
                e.getConstraintViolations()
                        .parallelStream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(Contants.DELIMITER));
        return ResponseEntity.badRequest()
                .body(new ErrorResponse().setErrorMsg(String.join(Contants.DELIMITER, message)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @RequestBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        log.error("[Marketplace Open Service] MethodArgumentNotValidException: {}", e.getMessage());
        List<String> messages = new ArrayList<>();
        e.getBindingResult()
                .getAllErrors()
                .forEach(
                        error -> {
                            String field = ((FieldError) error).getField();
                            String value = error.getDefaultMessage();
                            messages.add(field + ": " + value);
                        });

        return ResponseEntity.badRequest()
                .body(new ErrorResponse().setErrorMsg(String.join(Contants.DELIMITER, messages)));
    }

    @ExceptionHandler(BindException.class)
    @RequestBody
    public final ResponseEntity<ErrorResponse> hanldeBindException(BindException e) {
        log.error("[Marketplace Open Service] BindException: {}", e.getMessage());
        List<String> messages = new ArrayList<>();
        e.getBindingResult()
                .getAllErrors()
                .forEach(
                        error -> {
                            String field = ((FieldError) error).getField();
                            String value = error.getDefaultMessage();
                            messages.add(field + ": " + value);
                        });
        return ResponseEntity.badRequest()
                .body(new ErrorResponse().setErrorMsg(String.join(Contants.DELIMITER, messages)));
    }
}
