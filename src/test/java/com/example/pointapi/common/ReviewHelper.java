package com.example.pointapi.common;

import com.example.pointapi.place.domain.Place;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.review.repository.ReviewRepository;
import com.example.pointapi.user.domain.User;

public class ReviewHelper {

    public static Review 리뷰_생성(ReviewRepository reviewRepository, User user, Place place) {
        Review review = Review.createReview("content", user, place);
        return reviewRepository.save(review);
    }
}
