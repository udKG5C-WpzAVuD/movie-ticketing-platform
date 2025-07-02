package com.example.movieticketingplatform.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.dto.PageDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IOrdersService;
import com.example.movieticketingplatform.model.domain.Orders;

import java.util.ArrayList;
import java.util.List;


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
    @GetMapping("getOrders")
    public JsonResponse getAllOrders(Orders s,PageDTO pageDTO)throws Exception {
        Page<Orders> orders =ordersService.pageList(s,pageDTO) ;
        System.out.println(pageDTO.getPageNum()+" "+pageDTO.getPageSize());
        return JsonResponse.success(orders);
    }
    @GetMapping("searchOrders")
    public JsonResponse searchOrders(@RequestParam String orderNo)throws Exception {
        System.out.println("看这儿，进来了"+orderNo);
       List<Orders> orders= ordersService.getByOrderNo(orderNo);
        return JsonResponse.success(orders);
    }
    @GetMapping("fetchOrders")
    public JsonResponse fetchOrders()throws Exception {
        List<Orders> orders= ordersService.list();
        return JsonResponse.success(orders);
    }
    @PutMapping("refundOrder")
    public JsonResponse refundOrders(@RequestParam Long id)throws Exception {
        System.out.println("我来啦"+id);
        Orders orders = ordersService.getById(id);
        orders.setOrderStatus((byte) 3); // 强制转换为 byte
        ordersService.updateById(orders);
        return JsonResponse.success(orders);
    }
}

