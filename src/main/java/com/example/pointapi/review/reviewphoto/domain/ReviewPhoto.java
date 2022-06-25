package com.example.pointapi.review.reviewphoto.domain;

import com.example.pointapi.common.domain.BasicEntity;
import com.example.pointapi.review.domain.model.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
