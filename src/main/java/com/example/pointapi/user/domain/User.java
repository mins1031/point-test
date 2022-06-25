package com.example.pointapi.user.domain;

import com.example.pointapi.common.domain.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "user_table")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BasicEntity {

    private String name;

    private String uuidIdentifier;

    @Embedded
    private Point point;

    private User(String name, Point point) {
        this.name = name;
        this.uuidIdentifier = UUID.randomUUID().toString();
        this.point = point;
    }

    public static User createUser(String name, Point point) {
        return new User(name, point);
    }
}
