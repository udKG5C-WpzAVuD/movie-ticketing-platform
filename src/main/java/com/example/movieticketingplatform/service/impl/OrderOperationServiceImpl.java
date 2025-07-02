package com.example.movieticketingplatform.service.impl;

import com.example.movieticketingplatform.model.domain.OrderOperation;
import com.example.movieticketingplatform.mapper.OrderOperationMapper;
import com.example.movieticketingplatform.service.IOrderOperationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单操作记录表 服务实现类
 * </p>
 *
 * @author lxp
 * @since 2025-07-02
 */
@Service
public class OrderOperationServiceImpl extends ServiceImpl<OrderOperationMapper, OrderOperation> implements IOrderOperationService {

}
