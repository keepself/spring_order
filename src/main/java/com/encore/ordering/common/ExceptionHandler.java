package com.encore.ordering.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> entityNotFoundHandler(EntityNotFoundException e){
        log.error("Handler EntityNotFoundException Message : " + e.getMessage());
        return ErrorResponseDto.errorResponseMessage(HttpStatus.NOT_FOUND, e.getMessage()); // 404
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> illegalArgumentHandler(IllegalArgumentException e){
        log.error("Handler IllegalArgumentException Message : " + e.getMessage());
        return ErrorResponseDto.errorResponseMessage(HttpStatus.BAD_REQUEST, e.getMessage()); // 400
    }


}
