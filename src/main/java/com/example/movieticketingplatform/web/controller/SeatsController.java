package com.example.movieticketingplatform.web.controller;

import com.example.movieticketingplatform.model.domain.Sessions;
import com.example.movieticketingplatform.model.dto.SeatsDTO;
import jakarta.websocket.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.ISeatsService;
import com.example.movieticketingplatform.model.domain.Seats;

import java.util.ArrayList;
import java.util.List;


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
    @GetMapping("getseats")
    public JsonResponse<List<Seats>> getAllSeats( @RequestParam Long sessionId  )throws Exception {

        System.out.println("场次数据"+sessionId);
        List<Seats> seats = seatsService.findBySessionId(sessionId);
        return  JsonResponse.success(seats);
    }
    @PostMapping("updateSeats")
    public JsonResponse updateSeats(@RequestBody List<Seats> S)throws Exception {
        int count=0;
        System.out.println("我来啦"+S.size());
        for (Seats seats : S) {
            seats.setIsOccupied(true);
            boolean save=seatsService.updateById(seats);
            if(!save){
                count++;
            }
        }
        if(count>0){
            return JsonResponse.failure("传输失败");
        }else {
            return JsonResponse.success(count);
        }
    }
    @PutMapping("deleteSeats")
    public JsonResponse deleteSeats(@RequestBody SeatsDTO seatsDTO)throws Exception {
        System.out.println("删除数据"+seatsDTO.getCode());
        System.out.println(seatsDTO.getSessionId());
        Seats seats = seatsService.getBySessionandCode(seatsDTO.getSessionId(),seatsDTO.getCode());
        seats.setIsOccupied(false);
        seatsService.updateById(seats);
        return JsonResponse.success(seats);
    }
}

