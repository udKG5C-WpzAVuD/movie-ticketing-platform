package com.example.movieticketingplatform.web.controller;

import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.common.utls.SessionUtils;
import com.example.movieticketingplatform.model.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @GetMapping("getInfo")
    public JsonResponse getAuth(){
        User user = SessionUtils.getCurrentUserInfo();
        return  JsonResponse.success(user);
    }
}

