package com.example.pointapi.user.exception;

public class NotMatchPresentPoint extends RuntimeException {
    private static final String MESSAGE = "유저의 포인트와 유저의 포인트 기록 값이 일치하지 않습니다.";

    public NotMatchPresentPoint() {
        super(MESSAGE);
    }
}
