package com.example.movieticketingplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author lxp
 * @since 2025-07-01
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    Page<Orders> pageList(@Param("page")Page<Orders> page,  @Param("orders")Orders s);

    List<Orders> selectByOrderNo(String orderNo);
}
