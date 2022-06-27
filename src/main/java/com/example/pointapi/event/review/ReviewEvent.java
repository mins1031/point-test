package com.example.pointapi.event.review;

import com.example.pointapi.common.exception.WrongRequesterException;
import com.example.pointapi.event.Event;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.event.review.actions.AddActionExecutioner;
import com.example.pointapi.event.review.actions.DeleteActionExecutioner;
import com.example.pointapi.event.review.actions.ModActionExecutioner;
import com.example.pointapi.event.review.enums.ReviewAction;
import com.example.pointapi.event.review.exception.NotFoundReviewException;
import com.example.pointapi.place.domain.Place;
import com.example.pointapi.place.exception.NotFoundPlaceException;
import com.example.pointapi.place.repository.PlaceRepository;
import com.example.pointapi.pointrecord.domain.PointRecord;
import com.example.pointapi.pointrecord.repository.PointRecordRepository;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.review.repository.ReviewRepository;
import com.example.pointapi.user.domain.User;
import com.example.pointapi.user.domain.point.Point;
import com.example.pointapi.user.exception.NotFoundUserException;
import com.example.pointapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewEvent implements Event {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final PointRecordRepository pointRecordRepository;
    private final AddActionExecutioner addActionExecutioner;
    private final ModActionExecutioner modActionExecutioner;
    private final DeleteActionExecutioner deleteActionExecutioner;

    @Override
    @Transactional
    public void handlePoint(EventOccurRequest eventOccurRequest) {
        ReviewAction requestReviewAction = ReviewAction.catchReviewAction(eventOccurRequest.getReviewAction());
        Review review = reviewRepository.findByUuidIdentifier(eventOccurRequest.getReviewId()).orElseThrow(NotFoundReviewException::new);
        User user = userRepository.findByUuidIdentifier(eventOccurRequest.getUserId()).orElseThrow(NotFoundUserException::new);
        Place place = placeRepository.findByUuidIdentifier(eventOccurRequest.getPlaceId()).orElseThrow(NotFoundPlaceException::new);
        checkReviewerAndRequester(review, user);

        if (requestReviewAction.equals(ReviewAction.ADD)) {
            int addResultPoint = addActionExecutioner.addPoint(eventOccurRequest, review, place, user);
            savePointRecord(user, addResultPoint);
        }

        //todo 포인트 이력을 한꺼번에 할지 포인트 하나당 할지 생각해봐야함. 포인트 하나당 이력 객체를 생성해서 모아서 저장하는 방식도 있다.
        if (requestReviewAction.equals(ReviewAction.MODIFY)) {
            Point userPoint = user.getPoint();
            int resultPointByModify = modActionExecutioner.modifyPointRelatedContent(eventOccurRequest, userPoint) +
                    modActionExecutioner.modifyPointRelatedPhotos(eventOccurRequest, userPoint);
            savePointRecord(user, resultPointByModify);
        }

        if (requestReviewAction.equals(ReviewAction.DELETE)) {
            Point point = user.getPoint();
            savePointRecord(user, deleteActionExecutioner.deletePoint(point));
        }
    }

    private void checkReviewerAndRequester(Review review, User user) {
        if (!review.verifyReviewerAndRequester(user.getNum())) {
            throw new WrongRequesterException("요청된 리뷰의 정보와 유저의 정보가 다릅니다. 잘못된 요청입니다.");
        }
    }

    private void savePointRecord(User user, int resultPoint) {
        PointRecord pointRecord = PointRecord.createPointRecord(resultPoint, user.getPoint().getPresentPoint(), user);
        pointRecordRepository.save(pointRecord);
    }

}
