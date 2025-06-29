package com.example.movieticketingplatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.PageDTO;
import com.example.movieticketingplatform.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;


public interface IUserService extends IService<User> {

    Page<User> pageList(User user, PageDTO pageDTO);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @SuppressWarnings("unused")//确保前后端可以联动
    boolean updateUser(User user);

    @SuppressWarnings("unused")
    boolean toggleUserStatus(Long userId);
}
