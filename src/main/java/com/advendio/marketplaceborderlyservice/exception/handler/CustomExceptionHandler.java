package com.advendio.marketplaceborderlyservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e) {
        log.error("[Bolderly] Exception: {}", e.getMessage());
        return new ResponseEntity<Object>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

//    @ExceptionHandler(ParseException.class)
//    public final ResponseEntity<Object> handleParseException(ParseException e) {
//        log.error("[Bolderly] Exception: {}", e.getMessage());
//        return new ResponseEntity<Object>(e.getMessage(), HttpStatus.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler(BadJOSEException.class)
//    public final ResponseEntity<Object> handleBadJOSEException(Exception e) {
//        log.error("[Bolderly] Exception: {}", e.getMessage());
//        return new ResponseEntity<Object>(e.getMessage(), HttpStatus.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler(JOSEException.class)
//    public final ResponseEntity<Object> handleJOSEException(Exception e) {
//        log.error("[Bolderly] Exception: {}", e.getMessage());
//        return new ResponseEntity<Object>(e.getMessage(), HttpStatus.UNAUTHORIZED);
//    }
}
