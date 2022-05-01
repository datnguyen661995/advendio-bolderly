package com.advendio.marketplaceborderlyservice.exception.handler;

import com.advendio.marketplaceborderlyservice.client.handler.ResponseError;
import com.advendio.marketplaceborderlyservice.exception.CognitoException;
import com.advendio.marketplaceborderlyservice.exception.CustomErrorMessage;
import com.advendio.marketplaceborderlyservice.exception.CustomException;
import com.advendio.marketplaceborderlyservice.model.response.ErrorResponse;
import com.advendio.marketplaceborderlyservice.utils.Contants;
import com.advendio.marketplaceborderlyservice.utils.ResponseMessage;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e) {
        log.error("[Bolderly] Exception: {}", e.getMessage());
        return ResponseMessage.responseError();
    }

    @ExceptionHandler(CognitoException.class)
    public final ResponseEntity<Object> handleException(CognitoException e) {
        log.error("[Bolderly] Exception: {}", e.getMessage());
        return ResponseMessage.responseError(e);
    }

    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<Object> handleCustomException(CustomException e) {
        log.error("[Bolderly] CustomException: {}", e.getMessage());
        return ResponseMessage.responseError(e);
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public  final ResponseEntity<Object> handleSocketTimeoutException(SocketTimeoutException e) {
        log.error("[Bolderly] SocketTimeoutException: {}", e.getMessage());
        return ResponseMessage.responseError(new CustomException(HttpStatus.BAD_REQUEST, String.format(CustomErrorMessage.ERROR_API_TIME_OUT_NAME, e.getMessage())));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("[Bolderly] ConstraintViolationException: {}", e.getMessage());
        String message = e.getConstraintViolations()
                .parallelStream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(Contants.DELIMITER));
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse().setErrorMsg(String.join(Contants.DELIMITER, message)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @RequestBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("[Bolderly] MethodArgumentNotValidException: {}", e.getMessage());
        List<String> messages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String value = error.getDefaultMessage();
            messages.add(field + ": " + value);
        });

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse().setErrorMsg(String.join(Contants.DELIMITER,messages))
        );
    }

    @ExceptionHandler(BindException.class)
    @RequestBody
    public final ResponseEntity<ErrorResponse> hanldeBindException(BindException e) {
        log.error("[Bolderly] BindException: {}", e.getMessage());
        List<String> messages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String value = error.getDefaultMessage();
            messages.add(field + ": " + value);
        });
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse().setErrorMsg(String.join(Contants.DELIMITER,messages)));
    }
}
