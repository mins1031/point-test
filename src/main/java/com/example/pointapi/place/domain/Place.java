package com.example.pointapi.place.domain;

import com.example.pointapi.common.domain.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BasicEntity {

    private String name;

    private String uuidIdentifier;

    private Place(String name) {
        this.name = name;
        this.uuidIdentifier = UUID.randomUUID().toString();
    }

    public static Place createPlace(String name) {
        return new Place(name);
    }
}
