package com.example.pointapi.review.repository;

import com.example.pointapi.place.domain.Place;
import com.example.pointapi.review.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUuidIdentifier(String uuidIdentifier);

    Optional<Review> findByPlaceNum(Long aLong);

    long countByPlaceNum(Long placeNum);
}
