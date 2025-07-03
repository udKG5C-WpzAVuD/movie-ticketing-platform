package com.example.movieticketingplatform.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IUserActivityService;
import com.example.movieticketingplatform.model.domain.UserActivity;

import java.util.List;


@RestController
@RequestMapping("/api/userActivity")
public class UserActivityController {

    private final Logger logger = LoggerFactory.getLogger( UserActivityController.class );

    @Autowired
    private IUserActivityService userActivityService;

    @GetMapping("getUserActivity")
    public JsonResponse getUserActivity() throws Exception{
        List<UserActivity> userActivity = userActivityService.getUserActivity();
        return JsonResponse.success(userActivity);
    }
}

