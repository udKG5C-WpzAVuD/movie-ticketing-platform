package com.example.movieticketingplatform.service;

import lombok.Data;

/**
 * @Author
 * @Date Created in  2024/1/13 15:26
 * @DESCRIPTION: alipay接口参数
 * @Version V1.0
 */
@Data
public class AliPay {
    //订单编号
    private String traceNo;
    //商品金额
    private String totalAmount;
    //商品名称
    private String subject;
    //订单追踪号，商户自己生成，可已不使用
    private String alipayTraceNo;

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAlipayTraceNo() {
        return alipayTraceNo;
    }

    public void setAlipayTraceNo(String alipayTraceNo) {
        this.alipayTraceNo = alipayTraceNo;
    }
}
