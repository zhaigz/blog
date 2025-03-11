package com.zgz.service;

import com.zgz.pojo.User;

public interface UserService {
    User checkUser(String username, String password);
}
