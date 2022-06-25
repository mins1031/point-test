package com.example.pointapi.review.domain.model;

import com.example.pointapi.common.domain.BasicEntity;
import com.example.pointapi.place.domain.Place;
import com.example.pointapi.review.reviewphoto.domain.ReviewPhoto;
import com.example.pointapi.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BasicEntity {

    private String uuidIdentifier;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "review", orphanRemoval = true)
    private List<ReviewPhoto> reviewPhotos = new ArrayList<>();

    private Review(String content, User user, Place place, List<ReviewPhoto> reviewPhotos) {
        this.uuidIdentifier = UUID.randomUUID().toString();
        this.content = content;
        this.user = user;
        this.place = place;
        this.reviewPhotos = reviewPhotos;
    }

    public static Review createReview(String content, User user, Place place, List<ReviewPhoto> reviewPhotos) {
        return new Review(content, user, place, reviewPhotos);
    }
}
