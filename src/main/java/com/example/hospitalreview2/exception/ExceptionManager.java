package com.example.hospitalreview2.exception;

import com.example.hospitalreview2.domain.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(HospitalReviewException.class)
    public ResponseEntity<?> hospitalReviewExceptionHandler(HospitalReviewException e){
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }

    @ExceptionHandler(RuntimeException.class) //특정 exception을 받아서 처리할 수 있다.
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) { //ResponseEntity의 body에 모든 것이 들어갈 수 있다.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(e.getMessage()));
    }
}