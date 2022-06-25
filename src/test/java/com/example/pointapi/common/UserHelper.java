package com.example.pointapi.common;

import com.example.pointapi.user.domain.Point;
import com.example.pointapi.user.domain.User;
import com.example.pointapi.user.repository.UserRepository;

public class UserHelper {

    public static User 회원_생성(UserRepository userRepository, String userName) {
        User user = User.createUser(userName, Point.createPoint(0));
        return userRepository.save(user);
    }
}
