package com.example.pointapi.event.review;

import com.example.pointapi.common.exception.ImpossibleException;
import com.example.pointapi.common.exception.WrongRequesterException;
import com.example.pointapi.event.Event;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.event.review.enums.ReviewAction;
import com.example.pointapi.event.review.exception.NotFoundReviewException;
import com.example.pointapi.place.domain.Place;
import com.example.pointapi.place.exception.NotFoundPlaceException;
import com.example.pointapi.place.repository.PlaceRepository;
import com.example.pointapi.pointrecord.domain.PointRecord;
import com.example.pointapi.pointrecord.repository.PointRecordRepository;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.review.repository.ReviewRepository;
import com.example.pointapi.review.reviewphoto.domain.ReviewPhoto;
import com.example.pointapi.review.reviewphoto.exception.NotFoundReviewPhotoException;
import com.example.pointapi.review.reviewphoto.repository.ReviewPhotoRepository;
import com.example.pointapi.user.domain.User;
import com.example.pointapi.user.domain.point.Point;
import com.example.pointapi.user.domain.point.ReviewConditionChecker;
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

    @Autowired
    private PointRecordRepository pointRecordRepository;

    public static final int PLUS_COUNT_POINT = 1;
    public static final int MINUS_COUNT_POINT = -1;

    @Override
    @Transactional
    public void handlePoint(EventOccurRequest eventOccurRequest) {
        ReviewAction reviewAction = ReviewAction.catchReviewAction(eventOccurRequest.getReviewAction());
        Review review = reviewRepository.findByUuidIdentifier(eventOccurRequest.getReviewId()).orElseThrow(NotFoundReviewException::new);
        User user = userRepository.findByUuidIdentifier(eventOccurRequest.getUserId()).orElseThrow(NotFoundUserException::new);
        Place place = placeRepository.findByUuidIdentifier(eventOccurRequest.getPlaceId()).orElseThrow(NotFoundPlaceException::new);
        checkReviewerAndRequester(review, user);

        if (reviewAction.equals(ReviewAction.ADD)) {
            int addResultPoint = AddPoint(eventOccurRequest, review, place, user);
            savePointRecord(user, addResultPoint);
        }

        //todo 포인트 이력을 한꺼번에 할지 포인트 하나당 할지 생각해봐야함. 포인트 하나당 이력 객체를 생성해서 모아서 저장하는 방식도 있다.
        if (reviewAction.equals(ReviewAction.MODIFY)) {
            Point userPoint = user.getPoint();
            int resultPointByModify = modifyPointRelatedContent(eventOccurRequest, userPoint) + modifyPointRelatedPhotos(eventOccurRequest, userPoint);
            savePointRecord(user, resultPointByModify);
        }

        if (reviewAction.equals(ReviewAction.DELETE)) {
            Point point = user.getPoint();
            savePointRecord(user, deletePoint(point));
        }
    }

    private int deletePoint(Point point) {
        int tempPointCount = 0;
        if (point.getReviewConditionChecker().isContentPointState()) {
            point.getReviewConditionChecker().changeContentPointState(false);
            point.updatePresentPoint(MINUS_COUNT_POINT);
            tempPointCount -= 1;
        }

        if (point.getReviewConditionChecker().isPhotoPointState()) {
            point.getReviewConditionChecker().changePhotoPointState(false);
            point.updatePresentPoint(MINUS_COUNT_POINT);
            tempPointCount -= 1;
        }

        if (point.getReviewConditionChecker().isPlacePointState()) {
            point.getReviewConditionChecker().changePlacePointState(false);
            point.updatePresentPoint(MINUS_COUNT_POINT);
            tempPointCount -= 1;
        }

        return tempPointCount;
    }

    private void savePointRecord(User user, int resultPoint) {
        PointRecord pointRecord = PointRecord.createPointRecord(resultPoint, user.getPoint().getPresentPoint(), user);
        pointRecordRepository.save(pointRecord);
    }

    private int AddPoint(EventOccurRequest eventOccurRequest, Review review, Place place, User user) {
        //리뷰 패치조인하는 메서드 queryDsl로 구현할것.
        int tempPointCount = 0;

        if (eventOccurRequest.checkExistContent()) {
            user.getPoint().getReviewConditionChecker().changeContentPointState(true);
            tempPointCount += PLUS_COUNT_POINT;
        }

        if (eventOccurRequest.checkExistPhotos()) {
            verifyReviewPhoto(eventOccurRequest, review);
            user.getPoint().getReviewConditionChecker().changePhotoPointState(true);
            tempPointCount += PLUS_COUNT_POINT;
        }

        //todo 이쪽 count 성능이슈 생각해야됨
        if (reviewRepository.countByPlaceNum(place.getNum()) == 1) {
            verifyReviewPlace(review.getNum(), place.getNum());
            user.getPoint().getReviewConditionChecker().changePlacePointState(true);
            tempPointCount += PLUS_COUNT_POINT;
        }

        user.getPoint().updatePresentPoint(tempPointCount);
        return tempPointCount;
    }

    private void verifyReviewPhoto(EventOccurRequest eventOccurRequest, Review review) {
        List<String> attachedPhotoIds = eventOccurRequest.getAttachedPhotoIds();
        for (String attachedPhotoId : attachedPhotoIds) {
            if (attachedPhotoId != null) {
                ReviewPhoto reviewPhoto = reviewPhotoRepository.findByUuidIdentifier(attachedPhotoId).orElseThrow(NotFoundReviewPhotoException::new);
                reviewPhoto.verifyReviewer(review.getNum());
            }
        }
    }

    private void verifyReviewPlace(Long reviewNum, Long placeNum) {
        Review reviewByPlace = reviewRepository.findByPlaceNum(placeNum).orElseThrow(NotFoundReviewException::new);
        reviewByPlace.verifyReviewer(reviewNum);
    }

    private void checkReviewerAndRequester(Review review, User user) {
        if (!review.verifyReviewerAndRequester(user.getNum())) {
            throw new WrongRequesterException("요청된 리뷰의 정보와 유저의 정보가 다릅니다. 잘못된 요청입니다.");
        }
    }

    private int modifyPointRelatedContent(EventOccurRequest eventOccurRequest, Point user) {
        int tempPointCount = 0;
        //원래는 내용이 있었는데 삭제됐다.
        if (user.getReviewConditionChecker().isContentPointState() && !eventOccurRequest.checkExistContent()) {
            user.updatePresentPoint(MINUS_COUNT_POINT);
            user.getReviewConditionChecker().changeContentPointState(false);
            return tempPointCount -= 1;
        }

        //원래는 내용이 없었는데 추가됐다.
        if (!user.getReviewConditionChecker().isContentPointState() && eventOccurRequest.checkExistPhotos()) {
            user.updatePresentPoint(PLUS_COUNT_POINT);
            user.getReviewConditionChecker().changeContentPointState(true);
            return tempPointCount += 1;
        }

        return tempPointCount;
    }

    private int modifyPointRelatedPhotos(EventOccurRequest eventOccurRequest, Point point) {
        int tempPointCount = 0;
        //원래는 사진이 있었는데 삭제됐다.
        if (point.getReviewConditionChecker().isPhotoPointState() && !eventOccurRequest.checkExistPhotos()) {
            point.updatePresentPoint(MINUS_COUNT_POINT);
            point.getReviewConditionChecker().changePhotoPointState(false);
            tempPointCount -= 1;
        }

        //원래는 사진이 없었는데 추가됐다.
        if (!point.getReviewConditionChecker().isPhotoPointState() && eventOccurRequest.checkExistPhotos()) {
            point.updatePresentPoint(PLUS_COUNT_POINT);
            point.getReviewConditionChecker().changePhotoPointState(true);
            tempPointCount += 1;
        }
        
        return tempPointCount;
    }

}
