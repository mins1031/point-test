package com.example.pointapi.user.domain.point;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    private int presentPoint;

    @Embedded
    private ReviewConditionChecker reviewConditionChecker;

    public Point(int presentPoint, ReviewConditionChecker reviewConditionChecker) {
        this.presentPoint = presentPoint;
        this.reviewConditionChecker = reviewConditionChecker;
    }

    public static Point createPoint() {
        return new Point(0, new ReviewConditionChecker());
    }

    public void updatePresentPoint(int plusPointCount) {
        this.presentPoint += plusPointCount;
    }
}
