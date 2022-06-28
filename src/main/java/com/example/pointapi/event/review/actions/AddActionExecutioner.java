package com.example.pointapi.event.review.actions;

import com.example.pointapi.common.pointholder.PointScoreHolder;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.event.review.exception.NotFoundReviewException;
import com.example.pointapi.place.domain.Place;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.review.repository.ReviewRepository;
import com.example.pointapi.review.reviewphoto.domain.ReviewPhoto;
import com.example.pointapi.review.reviewphoto.exception.NotFoundReviewPhotoException;
import com.example.pointapi.review.reviewphoto.repository.ReviewPhotoRepository;
import com.example.pointapi.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddActionExecutioner {
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;

    public int addPoint(EventOccurRequest eventOccurRequest, Review review, Place place, User user) {
        int tempPointCount = 0;

        if (eventOccurRequest.checkExistContent()) {
            user.getPoint().getReviewConditionChecker().changeContentPointState(true);
            tempPointCount += PointScoreHolder.PLUS_COUNT_POINT;
        }

        if (eventOccurRequest.checkExistPhotos()) {
            verifyReviewPhoto(eventOccurRequest.getAttachedPhotoIds(), review.getNum());
            user.getPoint().getReviewConditionChecker().changePhotoPointState(true);
            tempPointCount += PointScoreHolder.PLUS_COUNT_POINT;
        }

        //todo 이쪽 count 성능이슈 생각해야됨
        if (reviewRepository.countByPlaceNum(place.getNum()) == 1) {
            verifyReviewPlace(review.getNum(), place.getNum());
            user.getPoint().getReviewConditionChecker().changePlacePointState(true);
            tempPointCount += PointScoreHolder.PLUS_COUNT_POINT;
        }

        user.getPoint().updatePresentPoint(tempPointCount);
        return tempPointCount;
    }

    private void verifyReviewPhoto(List<String> attachedPhotoIds, Long reviewNum) {
        for (String attachedPhotoId : attachedPhotoIds) {
            if (attachedPhotoId != null) {
                ReviewPhoto reviewPhoto = reviewPhotoRepository.findByUuidIdentifier(attachedPhotoId).orElseThrow(NotFoundReviewPhotoException::new);
                reviewPhoto.verifyReviewer(reviewNum);
            }
        }
    }

    private void verifyReviewPlace(Long reviewNum, Long placeNum) {
        Review reviewByPlace = reviewRepository.findByPlaceNum(placeNum).orElseThrow(NotFoundReviewException::new);
        reviewByPlace.verifyReviewer(reviewNum);
    }
}
