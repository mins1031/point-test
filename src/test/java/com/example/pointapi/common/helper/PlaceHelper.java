package com.example.pointapi.common.helper;

import com.example.pointapi.place.domain.Place;
import com.example.pointapi.place.repository.PlaceRepository;

public class PlaceHelper {

    public static Place 장소_생성(PlaceRepository placeRepository, String placeName) {
        Place place = Place.createPlace(placeName);
        return placeRepository.save(place);
    }
}
