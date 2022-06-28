package com.example.pointapi.user.service;

import com.example.pointapi.common.helper.PlaceHelper;
import com.example.pointapi.common.helper.ReviewHelper;
import com.example.pointapi.common.helper.UserHelper;
import com.example.pointapi.place.domain.Place;
import com.example.pointapi.place.repository.PlaceRepository;
import com.example.pointapi.pointrecord.domain.PointRecord;
import com.example.pointapi.pointrecord.repository.PointRecordRepository;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.review.repository.ReviewRepository;
import com.example.pointapi.user.domain.User;
import com.example.pointapi.user.dto.PointFindResponse;
import com.example.pointapi.user.exception.NotFoundUserException;
import com.example.pointapi.user.exception.NotMatchPresentPoint;
import com.example.pointapi.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("h2")
class PointFindServiceTest {

    @Autowired
    private PointFindService pointFindService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PointRecordRepository pointRecordRepository;

    @DisplayName("유저의 현재 포인트를 조회한다.")
    @Test
    void 유저_포인트_조회() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);

        user.getPoint().updatePresentPoint(3);
        user.getPoint().getReviewConditionChecker().changeContentPointState(true);
        user.getPoint().getReviewConditionChecker().changePhotoPointState(true);
        user.getPoint().getReviewConditionChecker().changePlacePointState(true);
        userRepository.save(user);

        PointRecord pointRecord = pointRecordRepository.save(PointRecord.createPointRecord(3, 3, user));

        //when
        PointFindResponse userPoint = pointFindService.findUserPoint(user.getUuidIdentifier());

        //then
        Assertions.assertThat(userPoint.getPresentPoint()).isEqualTo(3);
    }

    @DisplayName("[예외] 유저의 현재 포인트를 조회한다.")
    @Test
    void 예외_포인트_조회_없는유저() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);

        user.getPoint().updatePresentPoint(3);
        user.getPoint().getReviewConditionChecker().changeContentPointState(true);
        user.getPoint().getReviewConditionChecker().changePhotoPointState(true);
        user.getPoint().getReviewConditionChecker().changePlacePointState(true);
        userRepository.save(user);

        PointRecord pointRecord = pointRecordRepository.save(PointRecord.createPointRecord(3, 3, user));

        //when & then
        Assertions.assertThatThrownBy(() -> pointFindService.findUserPoint("not uuid")).isInstanceOf(NotFoundUserException.class);
    }

    @DisplayName("[예외] 유저포인트와 유저의 포인트 기록 정보가 일치하지 않는다.")
    @Test
    void 예외_유저포인트_포인트기록_불일치() {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);

        user.getPoint().updatePresentPoint(3);
        user.getPoint().getReviewConditionChecker().changeContentPointState(true);
        user.getPoint().getReviewConditionChecker().changePhotoPointState(true);
        user.getPoint().getReviewConditionChecker().changePlacePointState(true);
        userRepository.save(user);

        //when & then
        Assertions.assertThatThrownBy(() -> pointFindService.findUserPoint(user.getUuidIdentifier())).isInstanceOf(NotMatchPresentPoint.class);
    }
}