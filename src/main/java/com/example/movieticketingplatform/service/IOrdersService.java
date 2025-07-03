package com.example.movieticketingplatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieticketingplatform.model.dto.PageDTO;
import com.example.movieticketingplatform.model.domain.RefundRecords;

import java.util.List;
import java.math.BigDecimal;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author lxp
 * @since 2025-07-01
 */
public interface IOrdersService extends IService<Orders> {

    Page<Orders> pageList(Orders s, PageDTO pageDTO);

    List<Orders> getByOrderNo(String orderNo);
    Orders createOrder(Orders orders);
    Orders getByOrderNo(Orders orders);
    boolean cancelOrder(String orderNo);
    boolean updateOrderStatus(Orders orders);
    /**
     * 处理订单退款
     * @param orderNo 订单编号
     * @param refundAmount 退款金额
     * @param refundReason 退款原因
     * @return 退款记录（包含退款状态）
     */
    RefundRecords handleRefund(String orderNo, BigDecimal refundAmount, String refundReason);
}
