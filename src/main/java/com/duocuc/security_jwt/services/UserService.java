package com.duocuc.security_jwt.services;

import com.duocuc.security_jwt.models.users.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User save(User user);


}
