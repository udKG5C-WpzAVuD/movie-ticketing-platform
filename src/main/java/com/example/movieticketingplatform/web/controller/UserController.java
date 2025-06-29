package com.example.movieticketingplatform.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IUserService;
import com.example.movieticketingplatform.model.domain.User;



@RestController
@RequestMapping("/api/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger( UserController.class );

    @Autowired
    private IUserService userService;


    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse<User> getById(@PathVariable("id") Long id)throws Exception {
        User user = userService.getById(id);
        return JsonResponse.success(user);
    }

    @PostMapping("login")
    public JsonResponse login(@RequestBody User user)throws  Exception{
        User login = userService.login(user);
        return JsonResponse.success(login);
    }

}

