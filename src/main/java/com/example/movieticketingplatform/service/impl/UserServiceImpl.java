package com.example.movieticketingplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.movieticketingplatform.common.utls.SessionUtils;
import com.example.movieticketingplatform.model.domain.User;
import com.example.movieticketingplatform.mapper.UserMapper;
import com.example.movieticketingplatform.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword());
        User one = userMapper.selectOne(queryWrapper);
        // 把查到的用户存进session
        SessionUtils.saveCurrentUserInfo(one);
        return one;
    }
}
