package com.example.movieticketingplatform.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IRefundRecordsService;
import com.example.movieticketingplatform.model.domain.RefundRecords;


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
@RequestMapping("/api/refundRecords")
public class RefundRecordsController {

    private final Logger logger = LoggerFactory.getLogger( RefundRecordsController.class );

    @Autowired
    private IRefundRecordsService refundRecordsService;


    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse<RefundRecords> getById(@PathVariable("id") Long id)throws Exception {
        RefundRecords refundRecords = refundRecordsService.getById(id);
        return JsonResponse.success(refundRecords);
    }
}

