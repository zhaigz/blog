package com.zgz.service;

import com.zgz.dao.UserRepository;
import com.zgz.pojo.User;
import com.zgz.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserServiceImpl
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/8/31 16:26
 * @Version: 1.0
 **/
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
