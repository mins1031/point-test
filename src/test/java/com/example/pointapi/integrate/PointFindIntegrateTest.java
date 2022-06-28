package com.example.pointapi.integrate;

import com.example.pointapi.common.basetest.IntegrateBaseTest;
import com.example.pointapi.common.helper.PlaceHelper;
import com.example.pointapi.common.helper.ReviewHelper;
import com.example.pointapi.common.helper.UserHelper;
import com.example.pointapi.place.domain.Place;
import com.example.pointapi.pointrecord.domain.PointRecord;
import com.example.pointapi.review.domain.model.Review;
import com.example.pointapi.review.reviewphoto.domain.ReviewPhoto;
import com.example.pointapi.user.controller.PointFindControllerPath;
import com.example.pointapi.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PointFindIntegrateTest extends IntegrateBaseTest {

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
        PointRecord pointRecord = pointRecordRepository.save(PointRecord.createPointRecord(3, 3, user));

        user.getPoint().updatePresentPoint(3);
        user.getPoint().getReviewConditionChecker().changeContentPointState(true);
        user.getPoint().getReviewConditionChecker().changePhotoPointState(true);
        user.getPoint().getReviewConditionChecker().changePlacePointState(true);
        userRepository.save(user);

        //when & then
        mockMvc.perform(get(PointFindControllerPath.POINT_FIND.replace("{userId}", user.getUuidIdentifier()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("presentPoint").value(3))
                .andDo(print());
    }
}
