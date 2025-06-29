package com.example.movieticketingplatform.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.ISeatsService;
import com.example.movieticketingplatform.model.domain.Seats;


/**
 *
 *  前端控制器
 *
 *
 * @author lxp
 * @since 2025-06-28
 * @version v1.0
 */
@RestController
@RequestMapping("/api/seats")
public class SeatsController {

    private final Logger logger = LoggerFactory.getLogger( SeatsController.class );

    @Autowired
    private ISeatsService seatsService;


    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse<Seats> getById(@PathVariable("id") Long id)throws Exception {
        Seats seats = seatsService.getById(id);
        return JsonResponse.success(seats);
    }
}

