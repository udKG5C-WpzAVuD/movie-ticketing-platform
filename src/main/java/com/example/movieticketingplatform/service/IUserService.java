package com.example.movieticketingplatform.service;

import com.example.movieticketingplatform.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieticketingplatform.model.domain.dto.ResetPasswordDTO;
import com.example.movieticketingplatform.model.domain.dto.UserRegisterDTO;


public interface IUserService extends IService<User> {

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
}