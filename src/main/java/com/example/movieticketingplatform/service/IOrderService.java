package com.example.movieticketingplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieticketingplatform.model.domain.Orders;

import java.util.List;

public interface IOrderService extends IService<Orders> {
    List<Orders> listAllOrders();
}
