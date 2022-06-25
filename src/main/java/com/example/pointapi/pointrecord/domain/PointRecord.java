package com.example.pointapi.pointrecord.domain;

import com.example.pointapi.common.domain.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointRecord extends BasicEntity {

    private String uuidIdentifier;

    private String pointUpdateReason;

    private int pointUpdateCount;

    private int currentPointAfterUpdate;

    private PointRecord(String pointUpdateReason, int pointUpdateCount, int currentPointAfterUpdate) {
        this.uuidIdentifier = UUID.randomUUID().toString();
        this.pointUpdateReason = pointUpdateReason;
        this.pointUpdateCount = pointUpdateCount;
        this.currentPointAfterUpdate = currentPointAfterUpdate;
    }

    public static PointRecord createPointRecord(String pointUpdateReason, int pointUpdateCount, int currentPointAfterUpdate) {
        return new PointRecord(pointUpdateReason, pointUpdateCount, currentPointAfterUpdate);
    }
}
