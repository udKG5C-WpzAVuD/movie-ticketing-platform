package com.example.movieticketingplatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.PageDTO;
import com.example.movieticketingplatform.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import com.example.movieticketingplatform.model.domain.dto.ResetPasswordDTO;
import com.example.movieticketingplatform.model.domain.dto.UserRegisterDTO;

import java.io.IOException;
import java.util.List;


public interface IUserService extends IService<User> {

    Page<User> pageList(User user, PageDTO pageDTO);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @SuppressWarnings("unused")//确保前后端可以联动
    boolean updateUser(User user);

    @SuppressWarnings("unused")
    boolean toggleUserStatus(Long userId);

    User login(User user);
    /**
     * 发送注册验证码到邮箱
     * @param email 收件人邮箱
     */
    void sendRegisterCode(String email);
    void register(UserRegisterDTO registerDTO);
    // 新增：忘记密码相关方法
    /**
     * 发送密码重置验证码
     * @param email 注册邮箱
     */
    void sendResetPasswordCode(String email);

    /**
     * 验证验证码并重置密码
     * @param resetPasswordDTO 包含邮箱、验证码、新密码
     */
    void resetPassword(ResetPasswordDTO resetPasswordDTO);

    List<User> listAllUsers();

    List<User> getUsers();
}