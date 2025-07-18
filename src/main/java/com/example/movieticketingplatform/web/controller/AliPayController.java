package com.example.movieticketingplatform.web.controller;


import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.easysdk.factory.Factory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.movieticketingplatform.common.config.AliPayConfig;
import com.example.movieticketingplatform.model.domain.Orders;
import com.example.movieticketingplatform.service.AliPay;
import com.example.movieticketingplatform.service.impl.OrdersServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
/**
 * @Author
 * @Date Created in  2023/5/5 15:23
 * @DESCRIPTION:
 * @Version V1.0
 */
@RestController
@RequestMapping("alipay")
@Transactional(rollbackFor = Exception.class)
public class AliPayController {

    @Resource
    AliPayConfig aliPayConfig;
    @Autowired
    OrdersServiceImpl ordersService;

    private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT ="JSON";
    private static final String CHARSET ="utf-8";
    private static final String SIGN_TYPE ="RSA2";

    @GetMapping("/pay") // 前端路径参数格式?subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pay(AliPay aliPay, HttpServletResponse httpResponse) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl(aliPayConfig.getReturnUrl());
        request.setBizContent("{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","
                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","
                + "\"subject\":\"" + aliPay.getSubject() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\","
                + "\"timeout_express\":\"15m\"}"); // 关键：设置30分钟超时
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        // 直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request) throws Exception {
        if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
            System.out.println("=========支付宝异步回调========");

            // 1. 解析参数
            Map<String, String> params = new HashMap<>();
            request.getParameterMap().forEach((key, values) -> params.put(key, values[0]));

            String tradeNo = params.get("out_trade_no"); // 商户订单号
            String alipayTradeNo = params.get("trade_no"); // 支付宝交易号
            String gmtPayment = params.get("gmt_payment");

            // 2. 旧版SDK验签（与支付时的SDK一致）
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    aliPayConfig.getAlipayPublicKey(), // 支付宝公钥
                    CHARSET, // utf-8
                    SIGN_TYPE // RSA2
            );

            if (signVerified) { // 验签成功才更新订单
                Orders order = new Orders();
                order.setOrderNo(tradeNo);
                order.setOrderStatus((byte) 1); // 已支付
                // 兼容多种时间格式（避免解析失败）
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                try {
                    order.setPaymentTime(LocalDateTime.parse(gmtPayment, formatter));
                } catch (Exception e) {
                    order.setPaymentTime(LocalDateTime.now()); // 解析失败时用当前时间
                    System.out.println("支付时间解析失败，使用当前时间：" + e.getMessage());
                }
                order.setPaymentTransactionId(alipayTradeNo);
                order.setPaymentMethod("alipay");
                ordersService.updateOrderStatus(order); // 执行状态更新
            } else {
                System.out.println("支付宝回调验签失败！");
                return "fail"; // 验签失败，告知支付宝重新发送
            }
        }
        return "success";
    }
}
