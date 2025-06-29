package com.example.movieticketingplatform.service.impl;

import com.example.movieticketingplatform.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;

/**
 * @description: 邮件处理类
 * @author: luohanye
 * @create: 2019-04-19
 **/
@Service
public class MailServiceImpl implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSenderImpl mailSender;

    // 发件人邮箱（从配置文件读取）
    @Value("${spring.mail.username}")
    private String fromEmail;

    // 邮件标题（验证码场景固定）
    private static final String SUBJECT = "【电影购票平台】您的验证码为：";
    private static final String RESET_SUBJECT = "【电影购票平台】您的验证码为：";

    @Override
    public void sendVerifyCodeMail(String to, String verifyCode) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(SUBJECT);
            helper.setText(buildMailContent(verifyCode), true);
            mailSender.send(message);
            logger.info("验证码邮件已发送至：{}", to);

        } catch (MessagingException e) {
            // 关键：打印原始异常堆栈，显示具体错误（如授权失败、连接超时等）
            logger.error("发送验证码邮件失败！收件人：{}，错误详情：", to, e);
            throw new RuntimeException("验证码发送失败，请稍后重试");
        }
    }
    //发送密码重置验证码
    @Override
    public void sendResetPasswordCodeMail(String to, String verifyCode) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(RESET_SUBJECT);
            helper.setText(buildResetMailContent(verifyCode), true); // 专用邮件内容
            mailSender.send(message);
            logger.info("密码重置验证码邮件已发送至：{}", to);
        } catch (MessagingException e) {
            logger.error("发送密码重置验证码失败，收件人：{}", to, e);
            throw new RuntimeException("验证码发送失败");
        }
    }


    //构建密码重置邮件内容（与注册页风格一致）
    private String buildResetMailContent(String verifyCode) {
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;'>"
                + "<h2 style='color: #333;'>重置验证码</h2>"
                + "<p>您好！您正在进行密码重置操作，您的验证码为：</p>"
                + "<div style='font-size: 24px; font-weight: bold; color: #2c3e50; margin: 20px 0; padding: 10px; background: #f5f5f5; border-radius: 4px;'>"
                + verifyCode
                + "</div>"
                + "<p style='color: #666;'>验证码有效期为5分钟，请勿向他人泄露。如非本人操作，请忽略此邮件。</p>"
                + "</div>";
    }
    // 构建验证码邮件内容（HTML格式）
    private String buildMailContent(String verifyCode) {
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;'>"
                + "<h2 style='color: #333;'>注册验证码</h2>"
                + "<p>您好！您正在进行账号注册，您的验证码为：</p>"
                + "<div style='font-size: 24px; font-weight: bold; color: #2c3e50; margin: 20px 0; padding: 10px; background: #f5f5f5; border-radius: 4px;'>"
                + verifyCode
                + "</div>"
                + "<p style='color: #666;'>验证码有效期为5分钟，请勿向他人泄露。如非本人操作，请忽略此邮件。</p>"
                + "</div>";
    }
}