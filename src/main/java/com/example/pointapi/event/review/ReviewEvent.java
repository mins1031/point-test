package com.example.pointapi.event.review;

import com.example.pointapi.common.validator.ParameterValidator;
import com.example.pointapi.event.domain.Event;
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

    //리뷰액션은 종류가 3개에서 늘어날것 같지 않다고 생각되어 단순 if 분기로 처리했습니다.
    @Override
    @Transactional
    public void handlePoint(EventOccurRequest eventOccurRequest) {
        ReviewAction requestReviewAction = ReviewAction.catchReviewAction(eventOccurRequest.getAction());
        Review review = reviewRepository.findByUuidIdentifier(eventOccurRequest.getReviewId()).orElseThrow(NotFoundReviewException::new);
        User user = userRepository.findByUuidIdentifier(eventOccurRequest.getUserId()).orElseThrow(NotFoundUserException::new);
        Place place = placeRepository.findByUuidIdentifier(eventOccurRequest.getPlaceId()).orElseThrow(NotFoundPlaceException::new);
        ParameterValidator.checkReviewerAndRequester(review, user);

        if (requestReviewAction.equals(ReviewAction.ADD)) {
            int addResultPoint = addActionExecutioner.addPoint(eventOccurRequest, review, place, user);
            savePointRecord(user, addResultPoint);
        }

        if (requestReviewAction.equals(ReviewAction.MODIFY)) {
            Point userPoint = user.getPoint();
            int resultPointByModify = modActionExecutioner.modifyPointRelatedContent(eventOccurRequest, userPoint) + modActionExecutioner.modifyPointRelatedPhotos(eventOccurRequest, userPoint);
            savePointRecord(user, resultPointByModify);
        }

        if (requestReviewAction.equals(ReviewAction.DELETE)) {
            Point point = user.getPoint();
            savePointRecord(user, deleteActionExecutioner.deletePoint(point));
        }
    }

    private void savePointRecord(User user, int resultPoint) {
        PointRecord pointRecord = PointRecord.createPointRecord(resultPoint, user.getPoint().getPresentPoint(), user);
        pointRecordRepository.save(pointRecord);
    }
}
