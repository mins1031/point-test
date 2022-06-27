package com.example.pointapi.common;

import com.example.pointapi.place.domain.Place;
import com.example.pointapi.place.repository.PlaceRepository;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.review.repository.ReviewRepository;
import com.example.pointapi.review.reviewphoto.domain.ReviewPhoto;
import com.example.pointapi.review.reviewphoto.repository.ReviewPhotoRepository;
import com.example.pointapi.user.domain.User;
import com.example.pointapi.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class realdatainsert {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewPhotoRepository reviewPhotoRepository;

    @Test
    public void name() {
        User user = UserHelper.회원_생성(userRepository, "userName");
        Place place = PlaceHelper.장소_생성(placeRepository, "placeName");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);

        ReviewPhoto photo1 = reviewPhotoRepository.save(ReviewPhoto.createReviewPhoto("path1", review));
        ReviewPhoto photo2 = reviewPhotoRepository.save(ReviewPhoto.createReviewPhoto("path2", review));

        System.out.println(user.getUuidIdentifier());
        System.out.println(place.getUuidIdentifier());
        System.out.println(review.getUuidIdentifier());
        System.out.println(photo1.getUuidIdentifier());
        System.out.println(photo2.getUuidIdentifier());
    }
}
