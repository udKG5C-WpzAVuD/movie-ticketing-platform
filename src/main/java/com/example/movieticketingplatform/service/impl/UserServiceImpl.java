package com.example.movieticketingplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.PageDTO;
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
    public Page<User> pageList(User user, PageDTO pageDTO) {
        Page<User> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        page = userMapper.pageList(page,user);
        return page;
    }

    @Override
    public boolean existsByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        // 使用LambdaQueryWrapper构建查询条件
        return this.lambdaQuery()
                .eq(User::getUsername, username)
                .exists();  // 判断是否存在符合条件的记录
    }

    @Override
    public boolean existsByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        // 使用LambdaQueryWrapper构建查询条件
        return this.lambdaQuery()
                .eq(User::getEmail, email)
                .exists();  // 判断是否存在符合条件的记录
    }

}
