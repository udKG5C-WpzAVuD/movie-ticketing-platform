package com.example.movieticketingplatform.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.movieticketingplatform.common.RefundStatusEnum;
import com.example.movieticketingplatform.common.config.AliPayConfig;
import com.example.movieticketingplatform.mapper.SeatsMapper;
import com.example.movieticketingplatform.model.domain.Orders;
import com.example.movieticketingplatform.mapper.OrdersMapper;
import com.example.movieticketingplatform.model.dto.PageDTO;
import com.example.movieticketingplatform.model.domain.RefundRecords;
import com.example.movieticketingplatform.model.domain.Seats;
import com.example.movieticketingplatform.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieticketingplatform.service.IRefundRecordsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author lxp
 * @since 2025-07-01
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {
    @Autowired
    private SeatsMapper seatsMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired // 新增：注入支付宝配置
    private AliPayConfig aliPayConfig;
    @Autowired
    private IRefundRecordsService refundRecordsService; // 退款记录服务

    // 生成唯一订单号
    private String generateOrderNo() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "ORD" + time + random;
    }
    // 创建订单（含座位锁定）
    @Override
    @Transactional(rollbackFor = Exception.class) // 事务保证：座位锁定和订单创建要么同时成功，要么同时失败
    public Orders createOrder(Orders order) {
        // 1. 验证核心参数
        if (order.getUserId() == null || order.getSessionId() == null) {
            throw new RuntimeException("用户ID和场次ID不能为空");
        }
        if (order.getCode() == null || order.getCode().trim().isEmpty()) {
            throw new RuntimeException("请选择座位");
        }

        // 2. 解析前端传递的座位号（格式："A1,A2,B3"）
        String[] seatCodes = order.getCode().split(",");
        if (seatCodes.length == 0) {
            throw new RuntimeException("座位信息格式错误");
        }


        // 3. 验证座位是否存在且未被占用（核心逻辑）
        // 解析座位号时，强制转换为字符串数组
// 查询时明确参数类型
        List<Seats> seats = seatsMapper.selectList(new QueryWrapper<Seats>()
                .eq("session_id", order.getSessionId()) // session_id是Long类型
                .in("code", seatCodes)); // code是字符串数组


        this.ordersMapper = ordersMapper;
        // 检查是否有座位不存在（前端传递的座位号在数据库中找不到）
        if (seats.size() != seatCodes.length) {
            throw new RuntimeException("部分座位不存在，请重新选择");
        }

        // 检查是否有座位已被占用
        boolean hasOccupied = seats.stream().anyMatch(Seats::getIsOccupied);
        if (hasOccupied) {
            throw new RuntimeException("所选座位已被占用，请重新选择");
        }

        // 4. 锁定座位（更新为已占用状态）
        List<Integer> seatIds = seats.stream()
                .map(Seats::getSeatId) // 错误修复2：Seats的主键是seatId（原代码写的是getId，未定义）
                .collect(Collectors.toList());

        Seats updateSeat = new Seats();
        updateSeat.setIsOccupied(true); // 标记为已占用
        updateSeat.setUpdateTime(LocalDateTime.now()); // 更新时间戳

        // 错误修复3：QueryWrapper泛型应为Seats（与数据库表对应）
        int updateCount = seatsMapper.update(updateSeat, new QueryWrapper<Seats>()
                .in("seat_id", seatIds)); // 根据seat_id批量更新

        if (updateCount != seatIds.size()) {
            throw new RuntimeException("座位锁定失败，请重试");
        }

        // 5. 完善订单信息并保存
        order.setOrderNo(generateOrderNo()); // 生成唯一订单号
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setOrderStatus((byte) 0); // 0-待支付（状态枚举建议：0-待支付，1-已支付，2-已取消）

        // 错误修复4：确保总金额不为空（避免数据库非空约束报错）
        if (order.getTotalAmount() == null) {
            throw new RuntimeException("订单金额不能为空");
        }

        // 保存订单
        baseMapper.insert(order);

        return order;
    }

    // 根据订单号查询订单（用于订单详情页）
    @Override
    public Page<Orders> pageList(Orders s, PageDTO pageDTO) {
        Page<Orders> page = new Page<>(pageDTO.getPageNum(),pageDTO.getPageSize());
        page=ordersMapper.pageList(page,s);
        return page;
        }
    public Orders getByOrderNo(Orders orders) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getOrderNo, orders.getOrderNo());
        Orders one = ordersMapper.selectOne(queryWrapper);
        System.out.println("订单查询结果：" + one);
        return one;
    }

    // 取消订单（释放座位）
    @Override
    public List<Orders> getByOrderNo(String orderNo) {
        return baseMapper.selectByOrderNo(orderNo);}
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(String orderNo) {
        System.out.println("接收的订单号：" + orderNo); // 新增日志
        // 1. 查询订单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getOrderNo, orderNo);
        Orders order = ordersMapper.selectOne(queryWrapper);

        if (order == null) {
            System.out.println("订单不存在：" + orderNo); // 新增日志
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 0) {
            throw new RuntimeException("只有待支付订单可以取消");
        }

        // 2. 释放座位
        if (order.getCode() != null && !order.getCode().isEmpty()) {
            String[] seatCodes = order.getCode().split(",");

            Seats updateSeat = new Seats();
            updateSeat.setIsOccupied(false);
            updateSeat.setUpdateTime(LocalDateTime.now());

            int updateCount = seatsMapper.update(updateSeat, new QueryWrapper<Seats>()
                    .eq("session_id", order.getSessionId())
                    .in("code", seatCodes));

            if (updateCount != seatCodes.length) {
                throw new RuntimeException("座位释放失败，请重试");
            }
        }

        // 3. 更新订单状态
        order.setOrderStatus((byte) 2); // 2-已取消
        order.setUpdateTime(LocalDateTime.now());

        return updateById(order);
    }
    @Override
    public boolean updateOrderStatus(Orders order) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getOrderNo, order.getOrderNo());

        Orders update = new Orders();
        update.setOrderStatus(order.getOrderStatus());
        update.setPaymentTime(order.getPaymentTime());
        update.setPaymentTransactionId(order.getPaymentTransactionId());
        update.setUpdateTime(LocalDateTime.now());

        return baseMapper.update(update, queryWrapper) > 0;
    }
    @Override
    public RefundRecords handleRefund(String orderNo, BigDecimal refundAmount, String refundReason) {
        RefundRecords refundRecord = createRefundRecord(orderNo, refundAmount, refundReason);

        try {
            // 1. 查询订单（确保支付成功且有支付宝交易号）
            Orders order = getOne(new LambdaQueryWrapper<Orders>().eq(Orders::getOrderNo, orderNo));
            if (order == null) {
                throw new RuntimeException("订单不存在");
            }
            if (order.getOrderStatus() != 1) {
                throw new RuntimeException("仅已支付订单可退款（当前状态：" + getOrderStatusDesc(order.getOrderStatus()) + "）");
            }
            // 关键：验证支付时返回的支付宝交易号是否存在（支付成功必有的字段）
            if (order.getPaymentTransactionId() == null || order.getPaymentTransactionId().trim().isEmpty()) {
                throw new RuntimeException("订单未关联支付宝交易号，无法退款");
            }

            // 2. 校验退款金额
            if (refundAmount.compareTo(order.getTotalAmount()) > 0) {
                throw new RuntimeException("退款金额不能超过订单总金额（订单金额：" + order.getTotalAmount() + "）");
            }

            // 3. 释放座位（复用取消订单的逻辑，已验证正确）
            releaseSeatsInBatch(order);

            // 4. 调用支付宝退款接口（严格复用支付的配置）
            AlipayTradeRefundResponse refundResponse;
            try {
                // 复用支付时的客户端配置（网关、超时、密钥完全一致）
                refundResponse = callAlipayRefund(order.getPaymentTransactionId(), refundAmount);
            } catch (AlipayApiException e) {
                throw new RuntimeException("支付宝退款接口调用失败：" + e.getErrMsg(), e);
            }

            // 5. 验证退款响应
            if (!refundResponse.isSuccess()) {
                throw new RuntimeException("支付宝退款失败：" + refundResponse.getMsg() + "（错误码：" + refundResponse.getCode() + "）");
            }

            // 6. 更新订单状态
            order.setOrderStatus((byte) 3);
            order.setUpdateTime(LocalDateTime.now());
            updateById(order);

            // 7. 更新退款记录
            refundRecord.setRefundStatus(RefundStatusEnum.SUCCESS.getCode());
            refundRecord.setCompleteTime(LocalDateTime.now());
            refundRecordsService.updateById(refundRecord);

        } catch (Exception e) {
            refundRecord.setRefundStatus(RefundStatusEnum.FAIL.getCode());
            refundRecord.setRefundReason(refundReason + "（失败原因：" + e.getMessage() + "）");
            refundRecordsService.updateById(refundRecord);
            throw e;
        }

        return refundRecord;
    }


    // 辅助方法：创建初始退款记录
    private RefundRecords createRefundRecord(String orderNo, BigDecimal refundAmount, String refundReason) {
        RefundRecords record = new RefundRecords();
        record.setOrderId(getOrderIdByNo(orderNo)); // 关联订单ID
        record.setRefundNo(generateRefundNo()); // 生成唯一退款单号
        record.setRefundAmount(refundAmount);
        record.setRefundReason(refundReason);
        record.setRefundStatus(RefundStatusEnum.REFUNDING.getCode()); // 初始状态：退款中
        record.setCreateTime(LocalDateTime.now());
        record.setOperateId(1); // 操作人ID（实际应从登录信息获取）
        refundRecordsService.save(record); // 保存初始记录
        return record;
    }

    // 辅助方法：生成唯一退款单号（格式：REF+日期+随机数）
    private String generateRefundNo() {
        return "REF" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + RandomStringUtils.randomNumeric(6); // 6位随机数
    }

    // 辅助方法：调用支付宝退款接口
    // 辅助方法：调用支付宝退款接口（修正类型声明）
    private AlipayTradeRefundResponse callAlipayRefund(String alipayTradeNo, BigDecimal refundAmount) throws AlipayApiException {
        // 使用与支付完全一致的沙箱网关（带sandbox）
        String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
        System.out.println("退款网关地址：" + gatewayUrl); // 验证是否正确

        DefaultAlipayClient alipayClient = new DefaultAlipayClient(
                gatewayUrl,
                aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(),
                "JSON",
                "UTF-8",
                aliPayConfig.getAlipayPublicKey(),
                "RSA2"
        );
        alipayClient.setConnectTimeout(30000);
        alipayClient.setReadTimeout(30000);

        // 临时移除notifyUrl（避免HTTP协议导致的网关替换）
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        // request.setNotifyUrl(aliPayConfig.getNotifyUrl()); // 注释掉，先测试退款功能

        request.setBizContent("{" +
                "\"trade_no\":\"" + alipayTradeNo + "\"," +
                "\"refund_amount\":\"" + refundAmount.setScale(2) + "\"," +
                "\"refund_reason\":\"用户主动退单\"," +
                "\"out_request_no\":\"" + generateRefundNo() + "\"" +
                "}");

        AlipayTradeRefundResponse response = alipayClient.execute(request);
        System.out.println("退款响应：" + response.getBody());
        return response;
    }

    // 辅助方法：退款后更新订单（根据实际业务需求调整）
    private void updateOrderAfterRefund(Orders order, BigDecimal refundAmount) {
        // 示例：如果全额退款，标记订单状态为3（已退款）；部分退款可新增字段记录已退款金额
        if (refundAmount.compareTo(order.getTotalAmount()) == 0) { // 全额退款
            order.setOrderStatus((byte) 3); // 新增状态：3-已退款（需在原statusMap补充）
        } else { // 部分退款（可选：记录部分退款金额）
            // order.setRefundedAmount(refundAmount); // 需在Orders实体新增refundedAmount字段
        }
        order.setUpdateTime(LocalDateTime.now());
        updateById(order);
    }

    // 辅助方法：根据订单号获取订单ID
    private Integer getOrderIdByNo(String orderNo) {
        Orders order = getOne(new LambdaQueryWrapper<Orders>().eq(Orders::getOrderNo, orderNo));
        return order == null ? null : order.getId();
    }

    // 辅助方法：获取订单状态描述
    private String getOrderStatusDesc(Byte status) {
        switch (status) {
            case 0: return "待支付";
            case 1: return "已支付";
            case 2: return "已取消";
            case 3: return "已退款";
            default: return "未知状态";
        }
    }
    private void releaseSeatsInBatch(Orders order) {
        if (order.getCode() != null && !order.getCode().isEmpty()) {
            String[] seatCodes = order.getCode().split(",");

            Seats updateSeat = new Seats();
            updateSeat.setIsOccupied(false);
            updateSeat.setUpdateTime(LocalDateTime.now());

            int updateCount = seatsMapper.update(updateSeat, new QueryWrapper<Seats>()
                    .eq("session_id", order.getSessionId())
                    .in("code", seatCodes));

            if (updateCount != seatCodes.length) {
                throw new RuntimeException("座位释放失败，请重试");
            }
        }
    }
}
