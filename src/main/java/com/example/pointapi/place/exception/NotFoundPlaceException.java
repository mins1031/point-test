package com.example.pointapi.place.exception;

public class NotFoundPlaceException extends RuntimeException {
    private static final String MESSAGE = "장소가 존재하지 않습니다. 요청을 다시 확인해 주세요.";

    public NotFoundPlaceException() {
        super(MESSAGE);
    }
}
