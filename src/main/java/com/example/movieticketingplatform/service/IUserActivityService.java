package com.example.movieticketingplatform.service;

import com.example.movieticketingplatform.model.domain.UserActivity;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
