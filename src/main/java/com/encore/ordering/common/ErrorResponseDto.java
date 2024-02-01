package com.encore.ordering.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseDto {
    public static ResponseEntity<Map<String, Object>> errorResponseMessage(HttpStatus httpStatus, String message){
        // 객체는 JSON으로 직렬화 된다.
        Map<String, Object> body = new HashMap<>();
        body.put("status", String.valueOf(httpStatus.value()));
        body.put("status message", httpStatus.getReasonPhrase());
        body.put("error message", message);
        return new ResponseEntity<>(body, httpStatus);
    }
}
