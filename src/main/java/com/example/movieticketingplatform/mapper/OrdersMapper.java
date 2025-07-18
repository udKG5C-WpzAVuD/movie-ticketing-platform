package com.example.movieticketingplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.Orders;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author lxp
 * @since 2025-07-01
 */
public interface OrdersMapper extends BaseMapper<Orders> {
    @Select("SELECT SUM(total_amount) FROM orders WHERE create_time >= #{startDate} AND create_time <= #{endDate}")
    Double selectTotalAmount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') AS order_date, " +
            "COUNT(*) AS order_count FROM orders " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d') " +
            "ORDER BY order_date")
    List<Map<String, Object>> selectOrderCountsByDateRange(@Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);
    Page<Orders> pageList(@Param("page")Page<Orders> page,  @Param("orders")Orders s);

    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') AS order_date, " +
            "SUM(total_amount) AS total_revenue FROM orders " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d') " +
            "ORDER BY order_date")
    List<Map<String, Object>> selectTotalRevenueByDateRange(@Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate);
    List<Orders> selectByOrderNo(String orderNo);
}
