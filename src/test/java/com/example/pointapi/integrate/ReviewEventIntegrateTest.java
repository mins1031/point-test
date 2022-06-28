package com.example.pointapi.integrate;

import com.example.pointapi.common.basetest.IntegrateBaseTest;
import com.example.pointapi.common.helper.PlaceHelper;
import com.example.pointapi.common.helper.ReviewHelper;
import com.example.pointapi.common.helper.UserHelper;
import com.example.pointapi.event.EventType;
import com.example.pointapi.event.controller.EventControllerPath;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewEventIntegrateTest extends IntegrateBaseTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewPhotoRepository reviewPhotoRepository;

    @Autowired
    private PointRecordRepository pointRecordRepository;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("리뷰 포인트 추가 통합테스트")
    @Test
    public void 리뷰_포인트_추가_통합테스트() throws Exception {
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
        mockMvc.perform(post(EventControllerPath.EVENT_OCCUR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventOccurRequest))
                )
                .andExpectAll(status().isOk())
                .andDo(print());

        //then
        User savedUser = userRepository.findById(user.getNum()).get();
        Assertions.assertThat(savedUser.getPoint().getPresentPoint()).isEqualTo(3);

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
    }

    @DisplayName("리뷰 포인트 변경 통합테스트")
    @Test
    public void 리뷰_포인트_변경_통합테스트() throws Exception {
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
        mockMvc.perform(post(EventControllerPath.EVENT_OCCUR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventOccurRequest))
                )
                .andExpectAll(status().isOk())
                .andDo(print());

        //then
        User savedUser = userRepository.findById(user.getNum()).get();
        Assertions.assertThat(savedUser.getPoint().getPresentPoint()).isEqualTo(2);
        Assertions.assertThat(savedUser.getPoint().getReviewConditionChecker().isPhotoPointState()).isTrue();

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
    }

    @DisplayName("리뷰 포인트 삭제 통합테스트")
    @Test
    public void 리뷰_포인트_삭_통합테스트() throws Exception {
        //given
        User user = UserHelper.회원_생성(userRepository, "회원");
        Place place = PlaceHelper.장소_생성(placeRepository, "판교");
        Review review = ReviewHelper.리뷰_생성(reviewRepository, user, place);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.saveAll(Arrays.asList(
                ReviewPhoto.createReviewPhoto("path1", review),
                ReviewPhoto.createReviewPhoto("path2", review))
        );

        user.getPoint().updatePresentPoint(3);
        user.getPoint().getReviewConditionChecker().changeContentPointState(true);
        user.getPoint().getReviewConditionChecker().changePhotoPointState(true);
        user.getPoint().getReviewConditionChecker().changePlacePointState(true);
        userRepository.save(user);

        EventOccurRequest eventOccurRequest = new EventOccurRequest(
                EventType.REVIEW.getDesc(),
                ReviewAction.DELETE.getDesc(),
                review.getUuidIdentifier(),
                null,
                null,
                user.getUuidIdentifier(),
                place.getUuidIdentifier()
        );

        //when
        mockMvc.perform(post(EventControllerPath.EVENT_OCCUR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventOccurRequest))
                )
                .andExpectAll(status().isOk())
                .andDo(print());

        //then
        User savedUser = userRepository.findById(user.getNum()).get();
        Assertions.assertThat(savedUser.getPoint().getPresentPoint()).isEqualTo(0);
        Assertions.assertThat(savedUser.getPoint().getReviewConditionChecker().isContentPointState()).isFalse();
        Assertions.assertThat(savedUser.getPoint().getReviewConditionChecker().isPhotoPointState()).isFalse();
        Assertions.assertThat(savedUser.getPoint().getReviewConditionChecker().isPlacePointState()).isFalse();

        List<PointRecord> pointRecords = pointRecordRepository.findAll();
        Assertions.assertThat(pointRecords).hasSize(1);
    }

}
