package com.example.movieticketingplatform.service;

import com.example.movieticketingplatform.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IUserService extends IService<User> {

    User login(User user);
}
