package com.example.pointapi.review.domain.model;

import com.example.pointapi.common.domain.BasicEntity;
import com.example.pointapi.common.exception.ImpossibleException;
import com.example.pointapi.place.domain.Place;
import com.example.pointapi.user.domain.User;
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
//@Table(indexes = @Index(name = "idx_uuid_key", columnList = "uuid_identifier"))
public class Review extends BasicEntity {

    private String uuidIdentifier;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    private Review(String content, User user, Place place) {
        this.uuidIdentifier = UUID.randomUUID().toString();
        this.content = content;
        this.user = user;
        this.place = place;
    }

    public static Review createReview(String content, User user, Place place) {
        return new Review(content, user, place);
    }

    public boolean verifyReviewerAndRequester(Long userNum) {
        return user.getNum().equals(userNum);
    }

    public void verifyReviewer(Long reviewNum) {
        if (!this.getNum().equals(reviewNum)) {
            throw new ImpossibleException("리뷰정보와 장소리뷰의 정보가 맞지 않습니다.");
        }
    }
}
