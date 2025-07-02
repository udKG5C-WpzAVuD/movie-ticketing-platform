package com.example.movieticketingplatform.service.impl;

import com.example.movieticketingplatform.model.domain.UserActivity;
import com.example.movieticketingplatform.mapper.UserActivityMapper;
import com.example.movieticketingplatform.service.IUserActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserActivityServiceImpl extends ServiceImpl<UserActivityMapper, UserActivity> implements IUserActivityService {

    private final UserActivityMapper userActivityMapper;

    public UserActivityServiceImpl(UserActivityMapper userActivityMapper) {
        this.userActivityMapper = userActivityMapper;
    }

    @Override
    public List<UserActivity> getUserActivity() {
        List<UserActivity> userActivity = userActivityMapper.selectUserActivity();
        return userActivity == null ? new ArrayList<>() : userActivity;
    }
}
