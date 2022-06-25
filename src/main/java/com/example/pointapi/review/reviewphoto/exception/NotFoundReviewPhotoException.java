package com.example.pointapi.review.reviewphoto.exception;

public class NotFoundReviewPhotoException extends RuntimeException {
    private static final String MESSAGE = "리뷰 사진이 존재하지 않습니다. 요청을 다시 확인해 주세요.";

    public NotFoundReviewPhotoException() {
        super(MESSAGE);
    }
}
