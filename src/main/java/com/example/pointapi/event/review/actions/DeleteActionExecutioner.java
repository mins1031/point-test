package com.example.pointapi.event.review.actions;

import com.example.pointapi.common.pointholder.PointScoreHolder;
import com.example.pointapi.user.domain.point.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteActionExecutioner {

    public int deletePoint(Point point) {
        int tempPointCount = 0;
        if (point.getReviewConditionChecker().isContentPointState()) {
            point.getReviewConditionChecker().changeContentPointState(false);
            point.updatePresentPoint(PointScoreHolder.MINUS_COUNT_POINT);
            tempPointCount -= 1;
        }

        if (point.getReviewConditionChecker().isPhotoPointState()) {
            point.getReviewConditionChecker().changePhotoPointState(false);
            point.updatePresentPoint(PointScoreHolder.MINUS_COUNT_POINT);
            tempPointCount -= 1;
        }

        if (point.getReviewConditionChecker().isPlacePointState()) {
            point.getReviewConditionChecker().changePlacePointState(false);
            point.updatePresentPoint(PointScoreHolder.MINUS_COUNT_POINT);
            tempPointCount -= 1;
        }

        return tempPointCount;
    }
}
