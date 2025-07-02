package com.example.movieticketingplatform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.Orders;
import com.example.movieticketingplatform.mapper.OrdersMapper;
import com.example.movieticketingplatform.model.dto.PageDTO;
import com.example.movieticketingplatform.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private final OrdersMapper ordersMapper;

    public OrdersServiceImpl(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

    @Override
    public Page<Orders> pageList(Orders s, PageDTO pageDTO) {
        Page<Orders> page = new Page<>(pageDTO.getPageNum(),pageDTO.getPageSize());
        page=ordersMapper.pageList(page,s);
        return page;
    }

    @Override
    public List<Orders> getByOrderNo(String orderNo) {
        return baseMapper.selectByOrderNo(orderNo);
    }
}
