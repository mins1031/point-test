package com.example.pointapi.pointrecord.domain;

import com.example.pointapi.common.domain.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WrongPointRecord extends BasicEntity {

    private int userPoint;

    private int recordedPoint;

    private WrongPointRecord(int userPoint, int recordedPoint) {
        this.userPoint = userPoint;
        this.recordedPoint = recordedPoint;
    }

    public static WrongPointRecord createWrongPointRecord(int userPoint, int recordedPoint) {
        return new WrongPointRecord(userPoint, recordedPoint);
    }
}
