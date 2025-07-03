package com.example.movieticketingplatform.service;

import com.example.movieticketingplatform.model.domain.UserActivity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxp
 * @since 2025-06-30
 */
public interface IUserActivityService extends IService<UserActivity> {

    List<UserActivity> getUserActivity();
    // 更新用户购买记录（新增购买次数+1，累加总消费金额）
    void updatePurchaseActivity(Long userId, BigDecimal amount);
    void updateRefundActivity(Long userId, BigDecimal amount);
}
