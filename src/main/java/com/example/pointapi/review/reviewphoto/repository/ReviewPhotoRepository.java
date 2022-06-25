package com.example.pointapi.review.reviewphoto.repository;

import com.example.pointapi.review.reviewphoto.domain.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {
}
