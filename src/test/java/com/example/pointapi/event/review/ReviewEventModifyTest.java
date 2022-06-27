package com.example.pointapi.event.review;

import com.example.pointapi.common.PlaceHelper;
import com.example.pointapi.common.ReviewHelper;
import com.example.pointapi.common.UserHelper;
import com.example.pointapi.common.pointholder.PointScoreHolder;
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
class ReviewEventModifyTest {
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

    @DisplayName("MODIFY 액션. 사진이 없었다가 추가되어 1포인트 적립된다")
    @Test
    void MODIFY_사진추가() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.saveAll(Arrays.asList(
                ReviewPhoto.createReviewPhoto("path1", review),
                ReviewPhoto.createReviewPhoto("path2", review))
        );

        user.getPoint().updatePresentPoint(1);
        user.getPoint().getReviewConditionChecker().changeContentPointState(true);
        userRepository.save(user);

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.MODIFY.getDesc(),
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
        Assertions.assertThat(reviewer.getPoint().getReviewConditionChecker().isContentPointState()).isTrue();
        Assertions.assertThat(reviewer.getPoint().getReviewConditionChecker().isPhotoPointState()).isTrue();

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(1);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(2);
    }

    @DisplayName("MODIFY 액션. 내용이 없었다가 추가되어 1포인트 적립된다")
    @Test
    void MODIFY_내용추가() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.saveAll(Arrays.asList(
                ReviewPhoto.createReviewPhoto("path1", review),
                ReviewPhoto.createReviewPhoto("path2", review))
        );

        user.getPoint().updatePresentPoint(1);
        user.getPoint().getReviewConditionChecker().changePhotoPointState(true);
        userRepository.save(user);

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.MODIFY.getDesc(),
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
        Assertions.assertThat(reviewer.getPoint().getReviewConditionChecker().isContentPointState()).isTrue();
        Assertions.assertThat(reviewer.getPoint().getReviewConditionChecker().isPhotoPointState()).isTrue();

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(PointScoreHolder.PLUS_COUNT_POINT);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(2);
    }

    @DisplayName("MODIFY 액션. 사진이 있었다가 삭제되어 1포인트 감소된다")
    @Test
    void MODIFY_사진삭제() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.saveAll(Arrays.asList(
                ReviewPhoto.createReviewPhoto("path1", review),
                ReviewPhoto.createReviewPhoto("path2", review))
        );

        user.getPoint().updatePresentPoint(1);
        user.getPoint().getReviewConditionChecker().changePhotoPointState(true);
        userRepository.save(user);

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.MODIFY.getDesc(),
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
        Assertions.assertThat(reviewer.getPoint().getPresentPoint()).isEqualTo(0);
        Assertions.assertThat(reviewer.getPoint().getReviewConditionChecker().isContentPointState()).isFalse();

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(PointScoreHolder.MINUS_COUNT_POINT);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(0);
    }

    @DisplayName("MODIFY 액션. 내용이 있었다가 삭제되어 1포인트 감소된다")
    @Test
    void MODIFY_내용삭제() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);

        user.getPoint().updatePresentPoint(1);
        user.getPoint().getReviewConditionChecker().changeContentPointState(true);
        userRepository.save(user);

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.MODIFY.getDesc(),
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
        Assertions.assertThat(reviewer.getPoint().getPresentPoint()).isEqualTo(0);
        Assertions.assertThat(reviewer.getPoint().getReviewConditionChecker().isContentPointState()).isFalse();

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(PointScoreHolder.MINUS_COUNT_POINT);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(0);
    }

    @DisplayName("MODIFY 액션. 내용, 사진이 없었다가 추가되어 2포인트 적립된다")
    @Test
    void MODIFY_내용_사진_추가() {
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
                ReviewAction.MODIFY.getDesc(),
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
        Assertions.assertThat(reviewer.getPoint().getReviewConditionChecker().isContentPointState()).isTrue();
        Assertions.assertThat(reviewer.getPoint().getReviewConditionChecker().isPhotoPointState()).isTrue();

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(PointScoreHolder.PLUS_COUNT_POINT + PointScoreHolder.PLUS_COUNT_POINT);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(2);
    }

    @DisplayName("MODIFY 액션. 내용, 사진이 있었다가 삭제되어 2포인트 감소된다")
    @Test
    void MODIFY_내용_사진_삭제() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.saveAll(Arrays.asList(
                ReviewPhoto.createReviewPhoto("path1", review),
                ReviewPhoto.createReviewPhoto("path2", review))
        );

        user.getPoint().updatePresentPoint(2);
        user.getPoint().getReviewConditionChecker().changeContentPointState(true);
        user.getPoint().getReviewConditionChecker().changePhotoPointState(true);
        userRepository.save(user);

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.MODIFY.getDesc(),
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
        Assertions.assertThat(reviewer.getPoint().getPresentPoint()).isEqualTo(0);
        Assertions.assertThat(reviewer.getPoint().getReviewConditionChecker().isContentPointState()).isFalse();
        Assertions.assertThat(reviewer.getPoint().getReviewConditionChecker().isPhotoPointState()).isFalse();

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
        Assertions.assertThat(pointRecords.get(0).getUpdatedPoint()).isEqualTo(PointScoreHolder.MINUS_COUNT_POINT + PointScoreHolder.MINUS_COUNT_POINT);
        Assertions.assertThat(pointRecords.get(0).getCurrentPointAfterUpdate()).isEqualTo(0);
    }
}