package com.example.pointapi.user.domain.point;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class ReviewConditionChecker {
    private boolean contentPointState;
    private boolean photoPointState;
    private boolean placePointState;

    public ReviewConditionChecker() {
        this.contentPointState = false;
        this.photoPointState = false;
        this.placePointState= false;
    }

    public void changeContentPointState(boolean state) {
        this.contentPointState = state;
    }

    public void changePhotoPointState(boolean state) {
        this.photoPointState = state;
    }

    public void changePlacePointState(boolean state) {
        this.placePointState = state;
    }
}
