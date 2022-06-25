package com.example.pointapi.common;

import com.example.pointapi.place.domain.Place;
import com.example.pointapi.place.repository.PlaceRepository;
import com.example.pointapi.user.domain.Point;
import com.example.pointapi.user.domain.User;
import com.example.pointapi.user.repository.UserRepository;

public class PlaceHelper {

    public static Place 장소_생성(PlaceRepository placeRepository, String placeName) {
        Place place = Place.createPlace(placeName);
        return placeRepository.save(place);
    }
}
