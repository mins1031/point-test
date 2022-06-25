package com.example.pointapi.user.exception;

public class NotFoundUserException extends RuntimeException {
    private static final String MESSAGE = "회원이 존재하지 않습니다. 요청을 다시 확인해 주세요.";

    public NotFoundUserException() {
        super(MESSAGE);
    }
}
