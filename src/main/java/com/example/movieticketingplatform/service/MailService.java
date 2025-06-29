package com.example.movieticketingplatform.service;

public interface MailService {
    /**
     * 发送验证码邮件
     * @param to 收件人邮箱
     * @param verifyCode 6位数字验证码
     */
    void sendVerifyCodeMail(String to, String verifyCode);
    void sendResetPasswordCodeMail(String to, String verifyCode);//重置验证码
}
