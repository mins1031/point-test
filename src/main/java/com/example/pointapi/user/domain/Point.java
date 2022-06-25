package com.example.pointapi.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    private int presentPoint;

    private Point(int presentPoint) {
        this.presentPoint = presentPoint;
    }

    public static Point createPoint(int presentPoint) {
        return new Point(presentPoint);
    }

    public void updatePresentPoint(int plusPointCount) {
        this.presentPoint += plusPointCount;
    }
}
