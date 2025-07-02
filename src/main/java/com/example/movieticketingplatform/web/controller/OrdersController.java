package com.example.movieticketingplatform.web.controller;

import com.example.movieticketingplatform.common.RefundStatusEnum;
import com.example.movieticketingplatform.model.domain.RefundRecords;
import com.example.movieticketingplatform.model.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IOrdersService;
import com.example.movieticketingplatform.model.domain.Orders;

import java.math.BigDecimal;
import java.util.Map;


/**
 *
 *  前端控制器
 *
 *
 * @author lxp
 * @since 2025-07-01
 * @version v1.0
 */
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final Logger logger = LoggerFactory.getLogger( OrdersController.class );

    @Autowired
    private IOrdersService ordersService;


    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse<Orders> getById(@PathVariable("id") Long id)throws Exception {
        Orders orders = ordersService.getById(id);
        return JsonResponse.success(orders);
    }
    @PostMapping("/create")
    public JsonResponse<Orders> createOrder(@RequestBody Orders order) {
        try {
            Orders createdOrder = ordersService.createOrder(order);
            return JsonResponse.success(createdOrder, "订单创建成功");
        } catch (Exception e) {
            logger.error("创建订单失败", e);
            return JsonResponse.failure(StringUtils.defaultIfEmpty(e.getMessage(), "订单创建失败"));
        }
    }
    @PostMapping("/detail")
    public JsonResponse getByOrderNo(@RequestBody Orders orders) throws Exception {
        Orders getByOrderNo= ordersService.getByOrderNo(orders);
        return JsonResponse.success(getByOrderNo);
    }
    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    public JsonResponse cancelOrder(@RequestBody Map<String, String> params) {
        try {
            String orderNo = params.get("orderNo");
            if (StringUtils.isBlank(orderNo)) {
                return JsonResponse.failure("订单号不能为空");
            }

            boolean success = ordersService.cancelOrder(orderNo);
            if (success) {
                return JsonResponse.success("订单已取消");
            } else {
                return JsonResponse.failure("取消订单失败");
            }
        } catch (Exception e) {
            logger.error("取消订单失败", e);
            return JsonResponse.failure(StringUtils.defaultIfEmpty(e.getMessage(), "取消订单失败"));
        }
    }
    @PostMapping("/refund")
    public JsonResponse refundOrder(@RequestBody Map<String, String> params) {
        try {
            // 1. 参数校验
            String orderNo = params.get("orderNo"); // 订单编号
            String refundAmountStr = params.get("refundAmount"); // 退款金额
            String refundReason = params.get("refundReason"); // 退款原因（可选，默认"用户申请退款"）

            if (StringUtils.isBlank(orderNo)) {
                return JsonResponse.failure("订单编号不能为空");
            }
            if (StringUtils.isBlank(refundAmountStr)) {
                return JsonResponse.failure("退款金额不能为空");
            }
            // 解析退款金额（确保为正数）
            BigDecimal refundAmount;
            try {
                refundAmount = new BigDecimal(refundAmountStr);
                if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    return JsonResponse.failure("退款金额必须大于0");
                }
            } catch (NumberFormatException e) {
                return JsonResponse.failure("退款金额格式错误（示例：25.00）");
            }
            // 退款原因默认值
            refundReason = StringUtils.isBlank(refundReason) ? "用户申请退款" : refundReason;


            // 2. 调用服务层执行退款逻辑（核心）
            RefundRecords refundRecord = ordersService.handleRefund(orderNo, refundAmount, refundReason);


            // 3. 根据退款结果返回响应
            if (RefundStatusEnum.SUCCESS.getCode().equals(refundRecord.getRefundStatus())) {
                return JsonResponse.success("退款成功", refundRecord.getRefundNo()); // 返回退款单号
            } else if (RefundStatusEnum.REFUNDING.getCode().equals(refundRecord.getRefundStatus())) {
                return JsonResponse.success("退款申请已提交，处理中", refundRecord.getRefundNo());
            } else {
                return JsonResponse.failure("退款失败：" + refundRecord.getRefundReason());
            }

        } catch (Exception e) {
            logger.error("订单退款异常", e);
            return JsonResponse.failure("退款处理失败：" + e.getMessage());
        }
    }
}

