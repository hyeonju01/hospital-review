package com.example.hospitalreview2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated.");
    //검정색 굵은 글씨는 내가 맘대로 쓰는 것임. USERNAME_DUPLICATED()로 써두 됨

    private HttpStatus status;
    private String message;
}
