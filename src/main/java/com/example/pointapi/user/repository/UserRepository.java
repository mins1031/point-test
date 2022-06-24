package com.example.pointapi.user.repository;

import com.example.pointapi.place.domain.Place;
import com.example.pointapi.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<Place> findByUuidIdentifier(String uuidIdentifier);
}
