package com.example.pointapi.common.helper;

import com.example.pointapi.user.domain.point.Point;
import com.example.pointapi.user.domain.User;
import com.example.pointapi.user.repository.UserRepository;

public class UserHelper {

    public static User 회원_생성(UserRepository userRepository, String userName) {
        User user = User.createUser(userName);
        return userRepository.save(user);
    }
}
