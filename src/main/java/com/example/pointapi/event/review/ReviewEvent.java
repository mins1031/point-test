package com.example.pointapi.event.review;

import com.example.pointapi.common.exception.WrongRequesterException;
import com.example.pointapi.event.Event;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.event.review.enums.ReviewAction;
import com.example.pointapi.event.review.exception.NotFoundReviewException;
import com.example.pointapi.place.domain.Place;
import com.example.pointapi.place.exception.NotFoundPlaceException;
import com.example.pointapi.place.repository.PlaceRepository;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.review.repository.ReviewRepository;
import com.example.pointapi.review.reviewphoto.domain.ReviewPhoto;
import com.example.pointapi.review.reviewphoto.exception.NotFoundReviewPhotoException;
import com.example.pointapi.review.reviewphoto.repository.ReviewPhotoRepository;
import com.example.pointapi.user.domain.User;
import com.example.pointapi.user.exception.NotFoundUserException;
import com.example.pointapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewEvent implements Event {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewPhotoRepository reviewPhotoRepository;

    public static final int BASE_COUNT_POINT = 1;

    @Override
    @Transactional
    public void handlePoint(EventOccurRequest eventOccurRequest) {
        ReviewAction reviewAction = ReviewAction.catchReviewAction(eventOccurRequest.getReviewAction());
        Review review = reviewRepository.findByUuidIdentifier(eventOccurRequest.getReviewId()).orElseThrow(NotFoundReviewException::new);
        User user = userRepository.findByUuidIdentifier(eventOccurRequest.getUserId()).orElseThrow(NotFoundUserException::new);
        Place place = placeRepository.findByUuidIdentifier(eventOccurRequest.getPlaceId()).orElseThrow(NotFoundPlaceException::new);
        checkReviewerAndRequester(review, user);

        if (reviewAction.equals(ReviewAction.ADD)) {
            int updatePoint = AddPoint(eventOccurRequest, review, place);
            updateReviewPoint(user, updatePoint);
        }

        if (reviewAction.equals(ReviewAction.MODIFY)) {

        }

        if (reviewAction.equals(ReviewAction.DELETE)) {

        }
    }

    private int AddPoint(EventOccurRequest eventOccurRequest, Review review, Place place) {
        //리뷰 패치조인하는 메서드 queryDsl로 구현할것.
        int tempUpdatePoint = 0;

        if (eventOccurRequest.checkExistContent()) {
            tempUpdatePoint += BASE_COUNT_POINT;
        }

        if (eventOccurRequest.checkExistPhotos()) {
            tempUpdatePoint += verifyReviewPhoto(eventOccurRequest, review);
        }

        if (reviewRepository.countByPlaceNum(place.getNum()) == 1) {
            tempUpdatePoint += verifyReviewPlace(review, place);
        }

        return tempUpdatePoint;
    }

    private int verifyReviewPhoto(EventOccurRequest eventOccurRequest, Review review) {
        List<String> attachedPhotoIds = eventOccurRequest.getAttachedPhotoIds();
        for (String attachedPhotoId : attachedPhotoIds) {
            if (attachedPhotoId != null) {
                ReviewPhoto reviewPhoto = reviewPhotoRepository.findByUuidIdentifier(attachedPhotoId).orElseThrow(NotFoundReviewPhotoException::new);
                reviewPhoto.verifyReviewer(review.getNum());
            }
        }

        return BASE_COUNT_POINT;
    }

    private int verifyReviewPlace(Review review, Place place) {
        Review reviewByPlace = reviewRepository.findByPlaceNum(place.getNum()).orElseThrow(NotFoundReviewException::new);
        if (reviewByPlace.getNum() == review.getNum()) {
            return BASE_COUNT_POINT;
        }

        return 0;
    }

    private void checkReviewerAndRequester(Review review, User user) {
        if (!review.verifyReviewerAndRequester(user.getNum())) {
            throw new WrongRequesterException("요청된 리뷰의 정보와 유저의 정보가 다릅니다. 잘못된 요청입니다.");
        }
    }

    private void updateReviewPoint(User user, int updatePoint) {
        if (updatePoint == 0) {
            return;
        }

        user.getPoint().updatePresentPoint(updatePoint);
    }
}
