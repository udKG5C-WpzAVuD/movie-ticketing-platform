package com.example.movieticketingplatform.web.controller;

import com.example.movieticketingplatform.model.domain.dto.ResetPasswordDTO;
import com.example.movieticketingplatform.model.domain.dto.UserRegisterDTO;
import com.example.movieticketingplatform.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IUserService;
import com.example.movieticketingplatform.model.domain.User;

import javax.xml.transform.Result;
import java.util.Map;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;


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
    }//登录接口
    @PostMapping("login")
    public JsonResponse login(@RequestBody User user) throws Exception {
        User login = userService.login(user);
        return JsonResponse.success(login);
    }


    /**
     * 发送注册验证码
     * 修正：统一返回类型为JsonResponse<Object>，避免泛型不匹配
     */
    @PostMapping("/send-code")
    public JsonResponse<Object> sendRegisterCode(@RequestParam String email) { // 改为Object泛型
        userService.sendRegisterCode(email);
        return JsonResponse.success(null).setMessage("验证码已发送至邮箱，5分钟内有效");
    }
    /**
     * 注册接口
     */
    @PostMapping("/register")
    public JsonResponse<Object> register(@RequestBody UserRegisterDTO registerDTO) {
        userService.register(registerDTO);
        return JsonResponse.success(null).setMessage("注册成功，请登录");
    }//重置验证码接口
    @PostMapping("/forgot/send-code")
    public JsonResponse<Object> sendResetCode(@RequestParam String email) {
        userService.sendResetPasswordCode(email);
        return JsonResponse.success(null)
                .setMessage("密码重置验证码已发送至邮箱，5分钟内有效");
    }//重置密码接口
    @PostMapping("/forgot/reset-password")
    public JsonResponse<Object> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        userService.resetPassword(resetPasswordDTO);
        return JsonResponse.success(null)
                .setMessage("密码重置成功，请使用新密码登录");
    }
}