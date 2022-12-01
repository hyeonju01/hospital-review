package com.example.hospitalreview2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
이 부분은 정말 모르겠다,, 뭘 공부해야 할까?
 */
@AllArgsConstructor
@Getter
public class Response<T> {
    private String resultCode;
    private T result;

    // - error, success
    // static으로 선언하여 어디서든 사용 가능함
    public static Response<Void> error(String resultCode) {
        return new Response<>(resultCode, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }
}
