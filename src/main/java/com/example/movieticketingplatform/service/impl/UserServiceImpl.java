package com.example.movieticketingplatform.service.impl;

import com.example.movieticketingplatform.model.domain.User;
import com.example.movieticketingplatform.mapper.UserMapper;
import com.example.movieticketingplatform.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
