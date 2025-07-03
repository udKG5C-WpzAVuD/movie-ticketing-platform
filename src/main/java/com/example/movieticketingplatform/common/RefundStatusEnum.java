package com.example.movieticketingplatform.common;

import lombok.Getter;

/**
 * 退款状态枚举
 */
@Getter
public enum RefundStatusEnum {
    REFUNDING(0, "退款中"),
    SUCCESS(1, "退款成功"),
    FAIL(2, "退款失败");

    private final Integer code;
    private final String desc;

    RefundStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
