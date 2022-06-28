package com.example.pointapi.event.review.actions;

import com.example.pointapi.common.pointholder.PointScoreHolder;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.user.domain.point.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModActionExecutioner {

    public int modifyPointRelatedContent(EventOccurRequest eventOccurRequest, Point user) {
        int tempPointCount = 0;
        //원래는 내용이 있었는데 삭제됐다.
        if (user.getReviewConditionChecker().isContentPointState() && !eventOccurRequest.checkExistContent()) {
            user.updatePresentPoint(PointScoreHolder.MINUS_COUNT_POINT);
            user.getReviewConditionChecker().changeContentPointState(false);
            return tempPointCount -= 1;
        }

        //원래는 내용이 없었는데 추가됐다.
        if (!user.getReviewConditionChecker().isContentPointState() && eventOccurRequest.checkExistPhotos()) {
            user.updatePresentPoint(PointScoreHolder.PLUS_COUNT_POINT);
            user.getReviewConditionChecker().changeContentPointState(true);
            return tempPointCount += 1;
        }

        return tempPointCount;
    }

    public int modifyPointRelatedPhotos(EventOccurRequest eventOccurRequest, Point point) {
        int tempPointCount = 0;
        //원래는 사진이 있었는데 삭제됐다.
        if (point.getReviewConditionChecker().isPhotoPointState() && !eventOccurRequest.checkExistPhotos()) {
            point.updatePresentPoint(PointScoreHolder.MINUS_COUNT_POINT);
            point.getReviewConditionChecker().changePhotoPointState(false);
            return tempPointCount -= 1;
        }

        //원래는 사진이 없었는데 추가됐다.
        if (!point.getReviewConditionChecker().isPhotoPointState() && eventOccurRequest.checkExistPhotos()) {
            point.updatePresentPoint(PointScoreHolder.PLUS_COUNT_POINT);
            point.getReviewConditionChecker().changePhotoPointState(true);
            return tempPointCount += 1;
        }

        return tempPointCount;
    }
}
