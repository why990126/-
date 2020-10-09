package com.ocean.service;

import com.ocean.pojo.User;

public interface IUserService {
    //  用户注册
    public boolean registerUser(User user);

    // 用户登录
    public User login(String username,String password);
}
