package com.example.pointapi.user.domain;

import com.example.pointapi.common.domain.BasicEntity;
import com.example.pointapi.user.domain.point.Point;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(
        name = "user_table",
        indexes = @Index(name = "idx_uuid_key", columnList = "uuidIdentifier")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BasicEntity {

    private String name;

    private String uuidIdentifier;

    @Embedded
    private Point point;

    private User(String name) {
        this.name = name;
        this.uuidIdentifier = UUID.randomUUID().toString();
        this.point = Point.createPoint();
    }

    public static User createUser(String name) {
        return new User(name);
    }
}
