package com.example.pointapi.event.review;

import com.example.pointapi.common.helper.PlaceHelper;
import com.example.pointapi.common.helper.ReviewHelper;
import com.example.pointapi.common.helper.UserHelper;
import com.example.pointapi.common.exception.ImpossibleException;
import com.example.pointapi.common.exception.WrongRequesterException;
import com.example.pointapi.event.EventType;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.event.review.enums.ReviewAction;
import com.example.pointapi.place.domain.Place;
import com.example.pointapi.place.repository.PlaceRepository;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.review.repository.ReviewRepository;
import com.example.pointapi.review.reviewphoto.domain.ReviewPhoto;
import com.example.pointapi.review.reviewphoto.exception.NotFoundReviewPhotoException;
import com.example.pointapi.review.reviewphoto.repository.ReviewPhotoRepository;
import com.example.pointapi.user.domain.User;
import com.example.pointapi.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("h2")
class ReviewEventExceptionTest {
    @Autowired
    private ReviewEvent reviewEvent;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewPhotoRepository reviewPhotoRepository;

    @DisplayName("[예외] 리뷰의 userid와 파라미터로 조회한 유저의 userid가 다를 때.")
    @Test
    void 예외_요청자_리뷰어_NotEqual() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        User anotherUser = UserHelper.회원_생성(userRepository, "다른 회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.saveAll(Arrays.asList(
                ReviewPhoto.createReviewPhoto("path1", review),
                ReviewPhoto.createReviewPhoto("path2", review))
        );

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.ADD.getDesc(),
                review.getUuidIdentifier(),
                review.getContent(),
                reviewPhotos.stream().map(ReviewPhoto::getUuidIdentifier).collect(Collectors.toList()),
                anotherUser.getUuidIdentifier(),
                place.getUuidIdentifier()
        );

        //when & then
        Assertions.assertThatThrownBy(() -> reviewEvent.handlePoint(eventOccurRequest)).isInstanceOf(WrongRequesterException.class);
    }

    @DisplayName("[예외] 리뷰 사진이 등록되지 않은 id의 사진일 때")
    @Test
    void 예외_등록되지않은_리뷰사진() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.saveAll(Arrays.asList(
                ReviewPhoto.createReviewPhoto("path1", review),
                ReviewPhoto.createReviewPhoto("path2", review))
        );

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.ADD.getDesc(),
                review.getUuidIdentifier(),
                review.getContent(),
                Arrays.asList(UUID.randomUUID().toString()),
                user.getUuidIdentifier(),
                place.getUuidIdentifier()
        );

        //when & then
        Assertions.assertThatThrownBy(() -> reviewEvent.handlePoint(eventOccurRequest)).isInstanceOf(NotFoundReviewPhotoException.class);
    }

    @DisplayName("[예외] 리뷰 사진과 파라미터 사진의 리뷰가 다를 때")
    @Test
    void 예외_다른리뷰어_리뷰어사진() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        Review review2 = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.saveAll(Arrays.asList(
                ReviewPhoto.createReviewPhoto("path1", review),
                ReviewPhoto.createReviewPhoto("path2", review))
        );

        ReviewPhoto anotherReviewPhoto = reviewPhotoRepository.save(ReviewPhoto.createReviewPhoto("path3", review2));

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.ADD.getDesc(),
                review.getUuidIdentifier(),
                review.getContent(),
                Arrays.asList(anotherReviewPhoto.getUuidIdentifier()),
                user.getUuidIdentifier(),
                place.getUuidIdentifier()
        );

        //when & then
        Assertions.assertThatThrownBy(() -> reviewEvent.handlePoint(eventOccurRequest)).isInstanceOf(WrongRequesterException.class);
    }

    @DisplayName("[예외] 리뷰 장소와 파라미터 장소의 정보가 다를 때")
    @Test
    void 예외_리뷰장소_다른_파라미터장소() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        Place anotherPlace = PlaceHelper.장소_생성(placeRepository, "강남");
        Review anotherReview = ReviewHelper.리뷰_생성(reviewRepository, user, anotherPlace);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.saveAll(Arrays.asList(
                ReviewPhoto.createReviewPhoto("path1", review),
                ReviewPhoto.createReviewPhoto("path2", review))
        );

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.ADD.getDesc(),
                review.getUuidIdentifier(),
                review.getContent(),
                reviewPhotos.stream().map(ReviewPhoto::getUuidIdentifier).collect(Collectors.toList()),
                user.getUuidIdentifier(),
                anotherPlace.getUuidIdentifier()
        );

        //when & then
        Assertions.assertThatThrownBy(() -> reviewEvent.handlePoint(eventOccurRequest)).isInstanceOf(ImpossibleException.class);
    }

}