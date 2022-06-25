package com.example.pointapi.pointrecord.domain;

import com.example.pointapi.common.domain.BasicEntity;
import com.example.pointapi.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointRecord extends BasicEntity {

    private String uuidIdentifier;

    private int updatedPoint;

    private int currentPointAfterUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private PointRecord(int updatedPoint, int currentPointAfterUpdate, User user) {
        this.uuidIdentifier = UUID.randomUUID().toString();
        this.updatedPoint = updatedPoint;
        this.currentPointAfterUpdate = currentPointAfterUpdate;
        this.user = user;
    }

    public static PointRecord createPointRecord(int updatedPoint, int currentPointAfterUpdate, User user) {
        return new PointRecord(updatedPoint, currentPointAfterUpdate, user);
    }
}
