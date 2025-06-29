package com.example.movieticketingplatform.common.utls;

import java.security.SecureRandom;

public class VerifyCodeGenerator {
    // 验证码长度（6位数字）
    private static final int CODE_LENGTH = 6;

    /**
     * 生成6位随机数字验证码
     */
    public static String generate6DigitCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10)); // 生成0-9的随机数字
        }
        return code.toString();
    }
}