package com.example.movieticketingplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieticketingplatform.mapper.OrdersMapper;
import com.example.movieticketingplatform.model.domain.Orders;
import com.example.movieticketingplatform.service.IOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServicelmpl extends ServiceImpl<OrdersMapper, Orders> implements IOrderService {
    @Override
    public List<Orders> listAllOrders() {
        return list();
    }
}
