package com.example.pointapi.review.reviewphoto.domain;

import com.example.pointapi.common.domain.BasicEntity;
import com.example.pointapi.common.exception.WrongRequesterException;
import com.example.pointapi.review.domain.model.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_uuid_key", columnList = "uuidIdentifier"))
public class ReviewPhoto extends BasicEntity {

    private String uuidIdentifier;

    private String photoPath;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    private ReviewPhoto(String photoPath, Review review) {
        this.uuidIdentifier = UUID.randomUUID().toString();
        this.photoPath = photoPath;
        this.review = review;
    }

    public static ReviewPhoto createReviewPhoto(String photoPath, Review review) {
        return new ReviewPhoto(photoPath, review);
    }

    public void verifyReviewer(Long reviewerNum) {
        if (!this.review.getNum().equals(reviewerNum)) {
            throw new WrongRequesterException("요청자와 리뷰 사진의 리뷰어가 상이합니다. 잘못된 요청입니다.");
        }
    }
}
