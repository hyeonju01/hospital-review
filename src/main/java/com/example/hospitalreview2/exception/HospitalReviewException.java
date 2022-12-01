package com.example.hospitalreview2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
/*
Custom Exception 처리
 */
@AllArgsConstructor
@Getter
public class HospitalReviewException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;

    @Override
    public String toString() {
        if (message == null) return errorCode.getMessage();
        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}