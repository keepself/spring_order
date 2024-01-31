package com.encore.ordering.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerClass {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> entityNotFoundHandler(EntityNotFoundException e){
        log.error("Handler EntityNotFoundException Message : " + e.getMessage());
        return this.errorResponseMessage(HttpStatus.NOT_FOUND, e.getMessage()); // 404
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> illegalArgumentHandler(IllegalArgumentException e){
        log.error("Handler IllegalArgumentException Message : " + e.getMessage());
        return this.errorResponseMessage(HttpStatus.BAD_REQUEST, e.getMessage()); // 400
    }

    private ResponseEntity<Map<String, Object>> errorResponseMessage(HttpStatus httpStatus, String message){
        // 객체는 JSON으로 직렬화 된다.
        Map<String, Object> body = new HashMap<>();
        body.put("status", String.valueOf(httpStatus.value()));
        body.put("status message", httpStatus.getReasonPhrase());
        body.put("error message", message);
        return new ResponseEntity<>(body, httpStatus);
    }
}
