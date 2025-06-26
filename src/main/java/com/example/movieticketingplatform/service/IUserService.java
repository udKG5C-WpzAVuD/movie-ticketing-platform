package com.example.movieticketingplatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.PageDTO;
import com.example.movieticketingplatform.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IUserService extends IService<User> {

    Page<User> pageList(User user, PageDTO pageDTO);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
