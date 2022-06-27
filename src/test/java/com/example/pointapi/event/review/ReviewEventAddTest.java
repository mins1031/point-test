package com.example.pointapi.event.review;

import com.example.pointapi.common.PlaceHelper;
import com.example.pointapi.common.ReviewHelper;
import com.example.pointapi.common.UserHelper;
import com.example.pointapi.event.EventType;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.event.review.enums.ReviewAction;
import com.example.pointapi.place.domain.Place;
import com.example.pointapi.place.repository.PlaceRepository;
import com.example.pointapi.pointrecord.domain.PointRecord;
import com.example.pointapi.pointrecord.repository.PointRecordRepository;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.review.repository.ReviewRepository;
import com.example.pointapi.review.reviewphoto.domain.ReviewPhoto;
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
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("h2")
class ReviewEventAddTest {

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

    @Autowired
    private PointRecordRepository pointRecordRepository;

    @DisplayName("ADD 액션. content, photos, 첫리뷰 모두 적용, 포인트 3점 적립된다")
    @Test
    void ADD_내용_사진_첫리뷰_포인트적립() {
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
                reviewPhotos.stream().map(ReviewPhoto::getUuidIdentifier).collect(Collectors.toList()),
                user.getUuidIdentifier(),
                place.getUuidIdentifier()
        );

        //when
        reviewEvent.handlePoint(eventOccurRequest);

        //then
        User reviewer = userRepository.findById(user.getNum()).get();
        Assertions.assertThat(reviewer.getPoint().getPresentPoint()).isEqualTo(3);

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(3);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(3);
    }

    @DisplayName("ADD 액션. content, photos 적용, 2포인트 적립된다")
    @Test
    void ADD_내용_사진_포인트적립() {
        //given
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        User firstReviewer = UserHelper.회원_생성(userRepository, "회원");
        Review firstPlaceReview = ReviewHelper.리뷰_생성(reviewRepository, firstReviewer, place);

        User user = UserHelper.회원_생성(userRepository, "회원");
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
                user.getUuidIdentifier(),
                place.getUuidIdentifier()
        );

        //when
        reviewEvent.handlePoint(eventOccurRequest);

        //then
        User reviewer = userRepository.findById(user.getNum()).get();
        Assertions.assertThat(reviewer.getPoint().getPresentPoint()).isEqualTo(2);

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(2);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(2);
    }

    @DisplayName("ADD 액션. photos, 첫리뷰 적용, 2포인트 적립된다")
    @Test
    void ADD_사진_첫리뷰_포인트적립() {
        //given
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        User user = UserHelper.회원_생성(userRepository, "회원");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.saveAll(Arrays.asList(
                ReviewPhoto.createReviewPhoto("path1", review),
                ReviewPhoto.createReviewPhoto("path2", review))
        );

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.ADD.getDesc(),
                review.getUuidIdentifier(),
                null,
                reviewPhotos.stream().map(ReviewPhoto::getUuidIdentifier).collect(Collectors.toList()),
                user.getUuidIdentifier(),
                place.getUuidIdentifier()
        );

        //when
        reviewEvent.handlePoint(eventOccurRequest);

        //then
        User reviewer = userRepository.findById(user.getNum()).get();
        Assertions.assertThat(reviewer.getPoint().getPresentPoint()).isEqualTo(2);

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(2);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(2);
    }

    @DisplayName("ADD 액션. content, 첫리뷰 적용, 2포인트 적립된다")
    @Test
    void ADD_내용_첫리뷰_포인트적립() {
        //given
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        User user = UserHelper.회원_생성(userRepository, "회원");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.ADD.getDesc(),
                review.getUuidIdentifier(),
                review.getContent(),
                null,
                user.getUuidIdentifier(),
                place.getUuidIdentifier()
        );

        //when
        reviewEvent.handlePoint(eventOccurRequest);

        //then
        User reviewer = userRepository.findById(user.getNum()).get();
        Assertions.assertThat(reviewer.getPoint().getPresentPoint()).isEqualTo(2);

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(2);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(2);
    }

    @DisplayName("ADD 액션. content 적용, 1포인트 적립된다")
    @Test
    void ADD_내용_포인트적립() {
        //given
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        User firstReviewer = UserHelper.회원_생성(userRepository, "회원");
        Review firstPlaceReview = ReviewHelper.리뷰_생성(reviewRepository, firstReviewer, place);

        User user = UserHelper.회원_생성(userRepository, "회원");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.ADD.getDesc(),
                review.getUuidIdentifier(),
                review.getContent(),
                null,
                user.getUuidIdentifier(),
                place.getUuidIdentifier()
        );

        //when
        reviewEvent.handlePoint(eventOccurRequest);

        //then
        User reviewer = userRepository.findById(user.getNum()).get();
        Assertions.assertThat(reviewer.getPoint().getPresentPoint()).isEqualTo(1);

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(1);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(1);
    }

    @DisplayName("ADD 액션. photos 적용, 1포인트 적립된다")
    @Test
    void ADD_사진_포인트적립() {
        //given
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        User firstReviewer = UserHelper.회원_생성(userRepository, "회원");
        Review firstPlaceReview = ReviewHelper.리뷰_생성(reviewRepository, firstReviewer, place);

        User user = UserHelper.회원_생성(userRepository, "회원");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.saveAll(Arrays.asList(
                ReviewPhoto.createReviewPhoto("path1", review),
                ReviewPhoto.createReviewPhoto("path2", review))
        );

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.ADD.getDesc(),
                review.getUuidIdentifier(),
                null,
                reviewPhotos.stream().map(ReviewPhoto::getUuidIdentifier).collect(Collectors.toList()),
                user.getUuidIdentifier(),
                place.getUuidIdentifier()
        );

        //when
        reviewEvent.handlePoint(eventOccurRequest);

        //then
        User reviewer = userRepository.findById(user.getNum()).get();
        Assertions.assertThat(reviewer.getPoint().getPresentPoint()).isEqualTo(1);

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(1);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(1);
    }

    @DisplayName("ADD 액션. 첫리뷰 적용, 1포인트 적립된다")
    @Test
    void ADD_첫리뷰_포인트적립() {
        //given
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        User user = UserHelper.회원_생성(userRepository, "회원");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.ADD.getDesc(),
                review.getUuidIdentifier(),
                null,
                null,
                user.getUuidIdentifier(),
                place.getUuidIdentifier()
        );

        //when
        reviewEvent.handlePoint(eventOccurRequest);

        //then
        User reviewer = userRepository.findById(user.getNum()).get();
        Assertions.assertThat(reviewer.getPoint().getPresentPoint()).isEqualTo(1);

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(1);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(1);
    }
}