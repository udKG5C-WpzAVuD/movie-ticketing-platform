package com.example.movieticketingplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.movieticketingplatform.model.domain.UserActivity;
import com.example.movieticketingplatform.mapper.UserActivityMapper;
import com.example.movieticketingplatform.service.IUserActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserActivityServiceImpl extends ServiceImpl<UserActivityMapper, UserActivity> implements IUserActivityService {

    private final UserActivityMapper userActivityMapper;

    public UserActivityServiceImpl(UserActivityMapper userActivityMapper) {
        this.userActivityMapper = userActivityMapper;
    }

    @Override
    public List<UserActivity> getUserActivity() {
        List<UserActivity> userActivity = userActivityMapper.selectUserActivity();
        return userActivity == null ? new ArrayList<>() : userActivity;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchaseActivity(Long userId, BigDecimal amount) {
        // 1. 查询用户活动记录（不存在则创建）
        UserActivity activity = getOne(new LambdaQueryWrapper<UserActivity>()
                .eq(UserActivity::getUserId, userId));

        if (activity == null) {
            // 2. 新建用户活动记录
            activity = new UserActivity();
            activity.setUserId(userId);
            activity.setLoginCount(0); // 初始登录次数为0
            activity.setPurchaseCount(1); // 首次购买，次数=1
            activity.setTotalSpent(amount); // 总金额=当前订单金额
            activity.setLastActiveTime(LocalDateTime.now());
            activity.setGmtCreated(LocalDateTime.now());
            activity.setGmtModified(LocalDateTime.now());
            save(activity);
        } else {
            // 3. 已有记录，更新购买次数和总金额
            activity.setPurchaseCount(activity.getPurchaseCount() + 1); // 次数+1
            activity.setTotalSpent(activity.getTotalSpent().add(amount)); // 金额累加
            activity.setLastActiveTime(LocalDateTime.now()); // 更新最后活跃时间
            activity.setGmtModified(LocalDateTime.now());
            updateById(activity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRefundActivity(Long userId, BigDecimal amount) {
        // 退款逻辑：减少购买次数和总金额
        UserActivity activity = getOne(new LambdaQueryWrapper<UserActivity>()
                .eq(UserActivity::getUserId, userId));

        if (activity != null) {
            // 确保次数和金额不会为负数
            int newPurchaseCount = Math.max(0, activity.getPurchaseCount() - 1);
            BigDecimal newTotalSpent = activity.getTotalSpent().subtract(amount);
            newTotalSpent = newTotalSpent.compareTo(BigDecimal.ZERO) < 0 ?
                    BigDecimal.ZERO : newTotalSpent;

            activity.setPurchaseCount(newPurchaseCount);
            activity.setTotalSpent(newTotalSpent);
            activity.setLastActiveTime(LocalDateTime.now());
            updateById(activity);
        }
    }
}
