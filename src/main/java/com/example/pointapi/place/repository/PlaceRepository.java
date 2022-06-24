package com.example.pointapi.place.repository;

import com.example.pointapi.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByUuidIdentifier(String uuidIdentifier);
}
