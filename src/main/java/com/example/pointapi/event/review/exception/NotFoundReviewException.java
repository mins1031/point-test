package com.example.pointapi.event.review.exception;

public class NotFoundReviewException extends RuntimeException{
    private static final String MESSAGE = "리뷰가 존재하지 않습니다. 요청정보를 다시 확인해주세요.";

    public NotFoundReviewException() {
        super(MESSAGE);
    }
}
