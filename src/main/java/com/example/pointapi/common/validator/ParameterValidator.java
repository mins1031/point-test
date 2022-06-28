package com.example.pointapi.common.validator;

import com.example.pointapi.common.exception.WrongRequesterException;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.user.domain.User;

public class ParameterValidator {
    public static void checkReviewerAndRequester(Review review, User user) {
        if (!review.verifyReviewerAndRequester(user.getNum())) {
            throw new WrongRequesterException("요청된 리뷰의 정보와 유저의 정보가 다릅니다. 잘못된 요청입니다.");
        }
    }

    public static void checkUuidParameter(String uuidParam) {
        if (uuidParam == null || uuidParam.isEmpty() || uuidParam.isBlank()) {
            throw new WrongRequesterException("조회 식별자 정보는 필수 입니다.");
        }
    }

}
