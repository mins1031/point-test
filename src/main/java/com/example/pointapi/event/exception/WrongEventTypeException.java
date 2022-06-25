package com.example.pointapi.event.exception;

public class WrongEventTypeException extends RuntimeException {
    private static final String MESSAGE = "등록되지 않은 이벤트 타입입니다. 요청을 확인해 주세요.";

    public WrongEventTypeException() {
        super(MESSAGE);
    }
}
