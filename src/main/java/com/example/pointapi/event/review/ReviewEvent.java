package com.example.pointapi.event.review;

import com.example.pointapi.common.exception.WrongRequesterException;
import com.example.pointapi.event.Event;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.event.review.enums.ReviewAction;
import com.example.pointapi.event.review.exception.NotFoundReviewException;
import com.example.pointapi.place.repository.PlaceRepository;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.review.repository.ReviewRepository;
import com.example.pointapi.review.reviewphoto.repository.ReviewPhotoRepository;
import com.example.pointapi.user.domain.User;
import com.example.pointapi.user.exception.NotFoundUserException;
import com.example.pointapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void handlePoint(EventOccurRequest eventOccurRequest) {
        ReviewAction reviewAction = ReviewAction.catchReviewAction(eventOccurRequest.getReviewAction());
        int updatePoint = 0;

        if (reviewAction.equals(ReviewAction.ADD)) {
            //리뷰 패치조인하는 메서드 queryDsl로 구현할것.
            Review review = reviewRepository.findByUuidIdentifier(eventOccurRequest.getReviewId()).orElseThrow(NotFoundReviewException::new);
            User user = userRepository.findByUuidIdentifier(eventOccurRequest.getUserId()).orElseThrow(NotFoundUserException::new);
            checkReviewerAndRequester(review, user);

            eventOccurRequest
        }

        if (reviewAction.equals(ReviewAction.MODIFY)) {

        }

        if (reviewAction.equals(ReviewAction.DELETE)) {

        }
    }

    private void checkReviewerAndRequester(Review review, User user) {
        if (!review.verifyReviewerAndRequester(user.getNum())) {
            throw new WrongRequesterException("요청된 리뷰의 정보와 유저의 정보가 다릅니다. 잘못된 요청입니다.");
        }
    }
}
