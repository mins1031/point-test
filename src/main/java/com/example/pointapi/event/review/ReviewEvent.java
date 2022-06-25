package com.example.pointapi.event.review;

import com.example.pointapi.event.Event;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.event.review.enums.ReviewAction;
import com.example.pointapi.event.review.exception.WrongReviewActionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewEvent implements Event {


    @Override
    public void handlePoint(EventOccurRequest eventOccurRequest) {
        ReviewAction reviewAction = ReviewAction.catchReviewAction(eventOccurRequest.getReviewAction());

        if (reviewAction.equals(ReviewAction.ADD)) {

        }

        if (reviewAction.equals(ReviewAction.MODIFY)) {

        }

        if (reviewAction.equals(ReviewAction.DELETE)) {

        }
    }
}
