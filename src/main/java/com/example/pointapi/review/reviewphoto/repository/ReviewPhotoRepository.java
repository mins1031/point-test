package com.example.pointapi.review.reviewphoto.repository;

import com.example.pointapi.review.reviewphoto.domain.ReviewPhoto;
import com.example.pointapi.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {
    Optional<ReviewPhoto> findByUuidIdentifier(String uuidIdentifier);
}
