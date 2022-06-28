package com.example.pointapi.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointFindResponse {
    private int presentPoint;

    public PointFindResponse(int presentPoint) {
        this.presentPoint = presentPoint;
    }
}
