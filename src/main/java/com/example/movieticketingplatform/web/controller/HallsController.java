package com.example.movieticketingplatform.web.controller;

import com.example.movieticketingplatform.model.domain.Sessions;
import com.example.movieticketingplatform.model.dto.SessionDTO;
import com.example.movieticketingplatform.service.ISessionsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IHallsService;
import com.example.movieticketingplatform.model.domain.Halls;

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
@RequestMapping("/api/halls")
public class HallsController {

    private final Logger logger = LoggerFactory.getLogger( HallsController.class );

    @Autowired
    private IHallsService hallsService;


    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse<Halls> getById(@PathVariable("id") Long id)throws Exception {
        Halls halls = hallsService.getById(id);
        return JsonResponse.success(halls);
    }
//@GetMapping("getHalls")
//    public JsonResponse getHalls(SessionDTO s)throws Exception {
//    Sessions session = sessionsService.getHallsbys(s)
//
//        return JsonResponse.success(session);
//}
}

