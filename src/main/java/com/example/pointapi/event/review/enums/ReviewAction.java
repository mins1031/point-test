package com.example.pointapi.event.review.enums;

import com.example.pointapi.event.review.exception.WrongReviewActionException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReviewAction {
    ADD("ADD"),
    MODIFY("MOD"),
    DELETE("DELETE")
    ;

    private String desc;
//    private

    ReviewAction(String desc) {
        this.desc = desc;
    }

    public static ReviewAction catchReviewAction(String actionDesc) {
        return Arrays.stream(ReviewAction.values())
                .filter(reviewAction -> reviewAction.getDesc().equals(actionDesc))
                .findAny()
                .orElseThrow(WrongReviewActionException::new);
    }
}
