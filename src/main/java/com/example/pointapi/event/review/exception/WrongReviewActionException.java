package com.example.pointapi.event.review.exception;

public class WrongReviewActionException extends RuntimeException {
    private static final String MESSAGE = "등록되지 않은 리뷰동작입니다. 요청을 다시 확인해주세요.";

    public WrongReviewActionException() {
        super(MESSAGE);
    }
}
