package com.example.movieticketingplatform.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IUserActivityService;
import com.example.movieticketingplatform.model.domain.UserActivity;

import java.math.BigDecimal;
import java.util.List;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;


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
    /**
     * 支付成功后更新用户购买活动
     * @param userId 用户ID
     * @param amount 订单金额
     */
    @PostMapping("/update-purchase")
    public JsonResponse updatePurchaseActivity(
            @RequestParam Long userId,
            @RequestParam BigDecimal amount) {
        try {
            userActivityService.updatePurchaseActivity(userId, amount);
            return JsonResponse.success("用户活动记录更新成功");
        } catch (Exception e) {
            log.error("更新用户购买活动失败", e);
            return JsonResponse.failure("更新用户活动记录失败");
        }
    }
    @PostMapping("/update-refund")
    public JsonResponse updateRefundActivity(
            @RequestParam Long userId,
            @RequestParam BigDecimal amount
    ){
        try {
            userActivityService.updateRefundActivity(userId, amount);
            return JsonResponse.success("用户活动记录更新成功");
        }catch (Exception e){
            log.error("更新用户退款活动失败", e);
            return JsonResponse.failure("更新用户活动记录失败");
        }
    }
}

