package com.example.pointapi.review.repository;

import com.example.pointapi.review.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
