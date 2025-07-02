package com.example.movieticketingplatform.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IOrderOperationService;
import com.example.movieticketingplatform.model.domain.OrderOperation;


/**
 *
 *  前端控制器
 *
 *
 * @author lxp
 * @since 2025-07-02
 * @version v1.0
 */
@RestController
@RequestMapping("/api/orderOperation")
public class OrderOperationController {

    private final Logger logger = LoggerFactory.getLogger( OrderOperationController.class );

    @Autowired
    private IOrderOperationService orderOperationService;


    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse<OrderOperation> getById(@PathVariable("id") Long id)throws Exception {
        OrderOperation orderOperation = orderOperationService.getById(id);
        return JsonResponse.success(orderOperation);
    }
    @PostMapping("addorderoperation")
    public JsonResponse addOrderOperation(@RequestBody OrderOperation orderOperation)throws Exception {
        System.out.println("我进来啦"+orderOperation.getOperatorId());
         orderOperationService.save(orderOperation);
        return JsonResponse.success(orderOperation);
    }
}

