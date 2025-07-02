package com.example.movieticketingplatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieticketingplatform.model.dto.PageDTO;

import java.util.List;

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
}
