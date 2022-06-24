package com.example.pointapi.user.domain;

import com.example.pointapi.common.domain.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BasicEntity {

    private String name;

    private String publicNum;

    @Embedded
    private Point point;


}
